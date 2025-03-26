/*
 * @author: hasikiFire
 * @Date: 2024-06-04 11:13:28
 */
/*
 * @author: hasikiFire
 * @Date: 2024-06-04 11:13:28
 */
package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.core.common.constant.DatabaseConsts;
import com.hasikiFire.networkmall.core.common.constant.DatabaseConsts.RolesTable.RoleEnum;
import com.hasikiFire.networkmall.core.common.constant.RabbitMQConstants;
import com.hasikiFire.networkmall.core.common.constant.SendCodeTypeEnum;
import com.hasikiFire.networkmall.core.common.exception.BusinessException;
import com.hasikiFire.networkmall.core.common.req.UserDto;
import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.core.util.PasswordUtils;
import com.hasikiFire.networkmall.core.util.RedisUtil;
import com.hasikiFire.networkmall.core.util.SnowflakeDistributeId;
import com.hasikiFire.networkmall.core.util.TokenGenerator;
import com.hasikiFire.networkmall.core.util.YamlBuilder;
import com.hasikiFire.networkmall.dao.Proxy.ClashHttpProxy;
import com.hasikiFire.networkmall.dao.entity.Config;
import com.hasikiFire.networkmall.dao.entity.Link;
import com.hasikiFire.networkmall.dao.entity.Roles;
import com.hasikiFire.networkmall.dao.entity.UsageRecord;
import com.hasikiFire.networkmall.dao.entity.User;
import com.hasikiFire.networkmall.dao.entity.Wallet;
import com.hasikiFire.networkmall.dao.mapper.ConfigMapper;
import com.hasikiFire.networkmall.dao.mapper.LinkMapper;
import com.hasikiFire.networkmall.dao.mapper.RolesMapper;
import com.hasikiFire.networkmall.dao.mapper.UsageRecordMapper;
import com.hasikiFire.networkmall.dao.mapper.UserMapper;
import com.hasikiFire.networkmall.dao.mapper.WalletMapper;
import com.hasikiFire.networkmall.dto.req.UpdateUserReqDTO;
import com.hasikiFire.networkmall.dto.req.ForeignServerListReqDto;
import com.hasikiFire.networkmall.dto.req.SendMsgCodeToMqDto;
import com.hasikiFire.networkmall.dto.req.SendMsgCodeToMqParams;
import com.hasikiFire.networkmall.dto.req.UserCreateDto;
import com.hasikiFire.networkmall.dto.req.UserEditDto;
import com.hasikiFire.networkmall.dto.req.UserListReqDto;
import com.hasikiFire.networkmall.dto.req.UserLoginReqDto;
import com.hasikiFire.networkmall.dto.req.UserRegisterReqDto;
import com.hasikiFire.networkmall.dto.req.UsersendEmailCodeDto;
import com.hasikiFire.networkmall.dto.resp.ForeignServerListRespDto;
import com.hasikiFire.networkmall.dto.resp.SubscribeRespDto;
import com.hasikiFire.networkmall.dto.resp.UserInfoRespDto;
import com.hasikiFire.networkmall.dto.resp.UserListRespDto;
import com.hasikiFire.networkmall.dto.resp.UserLoginRespDto;
import com.hasikiFire.networkmall.dto.resp.UserRegisterRespDto;
import com.hasikiFire.networkmall.service.ForeignServerService;
import com.hasikiFire.networkmall.service.UserService;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ${hasikiFire}
 * @since 2024/06/03
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  @Value("${spring.mail.username}")
  private String username;

  private final WalletMapper walletMapper;
  private final RolesMapper roleMapper;
  private final UsageRecordMapper usageRecordMapper;
  private final ForeignServerService foreignServerService;
  private final ConfigMapper configMapper;
  private final LinkMapper linkMapper;
  private final UserMapper userMapper;
  private final RabbitTemplate rabbitTemplate;

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private JavaMailSender javaMailSender;

  @Override
  public RestResp<UserRegisterRespDto> register(UserRegisterReqDto dto) {
    String redisKey = SendCodeTypeEnum.REGISTER.getType() + ":" + dto.getEmail();
    String emailCode = (String) redisUtil.get(redisKey);

    // 校验验证码是否正确
    if (emailCode == null || !emailCode.equals(dto.getVelCode())) {
      // 验证码校验失败
      return RestResp.fail("验证码错误");
    }

    User user = createNewUser((UserDto) dto);
    // 删除验证码
    redisUtil.delete(redisKey);
    Roles role = new Roles();
    role.setUserId(user.getId());
    role.setRoleName(RoleEnum.USER.getRoleName());
    roleMapper.insert(role);

    StpUtil.login(user.getId());
    String token = StpUtil.getTokenValue();
    // 生成JWT 并返回
    return RestResp.ok(
        UserRegisterRespDto.builder()
            .token(token)
            .userID(user.getId())
            .build());
  }

  @Override
  public RestResp<UserLoginRespDto> login(UserLoginReqDto dto) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_EMAIL,
        dto.getEmail())
        .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
    User user = userMapper.selectOne(queryWrapper);
    if (user == null) {
      return RestResp.fail("邮箱或者密码错误");
    }
    String passwordHash = DigestUtils.md5DigestAsHex(
        (dto.getPassword() + user.getSalt()).getBytes(StandardCharsets.UTF_8));

    if (!passwordHash.equals(user.getPasswordHash())) {
      return RestResp.fail("邮箱或者密码错误");
    }

    StpUtil.login(user.getId());
    String token = StpUtil.getTokenValue();

    return RestResp.ok(
        UserLoginRespDto.builder()
            .token(token)
            .userID(user.getId())
            .build());

  }

  @Override
  public RestResp<UserInfoRespDto> getUserInfo(Long id) {
    User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, id));
    Roles roles = roleMapper.selectOne(new LambdaQueryWrapper<Roles>().eq(Roles::getUserId, id));
    if (user == null) {
      return RestResp.fail("用户不存在");
    }
    return RestResp.ok(
        UserInfoRespDto.builder()
            .userId(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .role(roles.getRoleName())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build());
  }

  @Override
  public RestResp<Void> sendEmailVerificationCode(UsersendEmailCodeDto emailDto) {
    try {
      String redisKey = emailDto.getType().getType() + ":" + emailDto.getEmail();
      String code = RandomStringUtils.randomNumeric(4);
      redisUtil.set(redisKey, code, 10, TimeUnit.MINUTES);
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(username);
      message.setTo(emailDto.getEmail());
      message.setSubject("验证码");
      message.setText("本次" + emailDto.getType().getDesc() + "为: " + code + "，有效期为 10 分钟");
      javaMailSender.send(message);
      return RestResp.ok();
    } catch (MailException e) {
      throw new BusinessException(e.getMessage());
    }
  }

  @Override
  public RestResp<PageRespDto<UserListRespDto>> getUserList(UserListReqDto params) {
    IPage<User> page = new Page<>();
    page.setCurrent(params.getPageNum());
    page.setSize(params.getPageSize());

    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
    queryWrapper.eq(User::getDeleted, 0);
    if (params.getUserId() != null) {
      queryWrapper.eq(User::getId, params.getUserId());
    }
    if (params.getName() != null) {
      queryWrapper.like(User::getName, params.getName());
    }
    if (params.getEmail() != null) {
      queryWrapper.like(User::getEmail, params.getEmail());
    }

    // queryWrapper.orderByDesc(User::getUpdatedAt);
    // IPage<User> usersPage = userMapper.selectPage(page, queryWrapper);
    // List<User> users = usersPage.getRecords();
    // queryWrapper.orderByDesc(User::getUpdatedAt);
    IPage<User> usersPage = userMapper.selectPage(page, queryWrapper);
    List<User> users = usersPage.getRecords();

    // 2. 获取所有的userId
    List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
    log.info("User IDs: {}", userIds);
    // 3. 根据userId查询wallet表, 取出balance和currency字段
    List<Wallet> wallets = walletMapper.selectList(new QueryWrapper<Wallet>().in("user_id", userIds)
        .select("user_id", "balance", "currency"));
    log.info("User wallets: {}", wallets);
    // 4. 根据userId查询UsageRecord表，筛选出purchaseStatus为1的记录
    List<UsageRecord> usageRecords = usageRecordMapper.selectList(new QueryWrapper<UsageRecord>().in("user_id", userIds)
        .eq("purchase_status", 1));

    log.info("User usageRecords: {}", usageRecords);

    Map<Long, Wallet> userIdToWalletMap = wallets.stream()
        .collect(Collectors.toMap(Wallet::getUserId, wallet -> wallet));
    Map<Long, List<UsageRecord>> userIdToPackageRecordsMap = usageRecords.stream()
        .collect(Collectors.groupingBy(UsageRecord::getUserId));

    List<UserListRespDto> userListRespDtos = users.stream().map(user -> {
      Long userId = user.getId();
      Wallet userWallet = userIdToWalletMap.get(userId);
      List<UsageRecord> userPackageRecords = userIdToPackageRecordsMap.getOrDefault(userId,
          new ArrayList<>());

      return UserListRespDto.builder()
          .userId(user.getId())
          .name(user.getName())
          .email(user.getEmail())
          .status(user.getStatus())
          .createdAt(user.getCreatedAt())
          .updatedAt(user.getUpdatedAt())

          .balance(userWallet != null ? userWallet.getBalance() : null)
          .currency(userWallet != null ? userWallet.getCurrency() : null)
          .usageRecord(userPackageRecords)
          .build();
    }).collect(Collectors.toList());

    return RestResp.ok(
        PageRespDto.of(params.getPageNum(), params.getPageSize(), page.getTotal(), userListRespDtos));
    // return RestResp.ok(
    // PageRespDto.of(params.getPageNum(), params.getPageSize(), page.getTotal(),
    // users.stream().map(user -> UserListRespDto.builder()
    // .userId(user.getUserId())
    // .name(user.getName())
    // .email(user.getEmail())
    // .status(user.getStatus())
    // .createdAt(user.getCreatedAt())
    // .updatedAt(user.getUpdatedAt())
    // .build()).collect(Collectors.toList())));

  }

  @Override
  public RestResp<User> updateUser(UserEditDto dto) {
    User user = null;
    if (dto.getUserId() == null) {
      user = createNewUser((UserDto) dto);
    } else {
      LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.eq(User::getId, dto.getUserId());
      user = userMapper.selectOne(queryWrapper);
      log.info("User: selectOne {}", user);
      if (user == null) {
        return RestResp.fail("用户不存在");
      } else {
        user = updateNewUser(dto, user);
      }
    }

    if (user == null) {
      return RestResp.fail("更新失败");
    }

    return RestResp.ok(user);

  }

  private User updateNewUser(UserEditDto newUserDtoser, User oldUser) {
    log.info("User: newUserDtoser {}", newUserDtoser);
    // 注册成功，保存用户信息

    if (newUserDtoser.getPassword() != null) {
      String salt = PasswordUtils.generateSalt();
      String passwordHash = DigestUtils.md5DigestAsHex(
          (newUserDtoser.getPassword() + salt).getBytes(StandardCharsets.UTF_8));
      oldUser.setPasswordHash(
          passwordHash);
      oldUser.setSalt(salt);
    }
    if (newUserDtoser.getName() != null) {
      oldUser.setName(newUserDtoser.getName());
    }
    if (newUserDtoser.getInviteCode() != null) {
      oldUser.setInviteCode(newUserDtoser.getInviteCode());
    }
    if (newUserDtoser.getEmail() != null) {
      if (!newUserDtoser.getEmail().equals(oldUser.getEmail())) {
        throw new BusinessException("新邮箱与旧邮箱一致，无需修改");
      }
      // 校验邮箱是否已注册
      QueryWrapper<User> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_EMAIL,
          newUserDtoser.getEmail())
          .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
      if (userMapper.selectCount(queryWrapper) > 0) {
        throw new BusinessException("邮箱已被注册");
      }
      oldUser.setEmail(newUserDtoser.getEmail());
    }
    if (newUserDtoser.getStatus() != null) {
      oldUser.setStatus(newUserDtoser.getStatus());
    }

    userMapper.updateById(oldUser);
    log.info("User: updateNewUser {}", oldUser);

    return oldUser;
  }

  private User createNewUser(UserDto dto) {
    // 校验邮箱是否已注册
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_EMAIL,
        dto.getEmail())
        .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
    if (userMapper.selectCount(queryWrapper) > 0) {
      throw new BusinessException("邮箱已被注册");
    }

    SnowflakeDistributeId idWorker = new SnowflakeDistributeId(0, 0);

    // Long userId = userIdModal.getUserId();
    // 注册成功，保存用户信息
    User user = new User();
    if (dto.getPassword() == null) {
      throw new BusinessException("密码不能为空");
    }
    if (dto.getEmail() == null) {
      throw new BusinessException("邮箱不能为空");
    }

    String salt = PasswordUtils.generateSalt();
    String passwordHash = DigestUtils.md5DigestAsHex(
        (dto.getPassword() + salt).getBytes(StandardCharsets.UTF_8));
    user.setPasswordHash(
        passwordHash);
    user.setName(dto.getName() == null ? "User" : dto.getName());
    user.setEmail(dto.getEmail());
    user.setUuid(idWorker.nextId());
    user.setStatus(1);
    user.setSalt(salt);
    userMapper.insert(user);
    return user;
  }

  @Override
  public RestResp<Boolean> updateUserStatus(UpdateUserReqDTO req) {
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(User::getId, req.getUserID());
    User user = userMapper.selectOne(queryWrapper);
    if (user == null) {
      return RestResp.fail("用户不存在");
    }

    if (req.getStatus() != null) {
      if (req.getStatus() == 1) {
        // 删除用户
        user.setStatus(1);
        userMapper.updateById(user);
      }
      if (req.getStatus() == 2) {
        // 删除用户
        user.setStatus(2);
        userMapper.updateById(user);
        SendMQMsg("deleteUser", user.getId());
      }
    }
    if (req.getIsDelete() != null && req.getIsDelete() == 1) {
      // 删除用户
      user.setDeleted(1);
      user.setStatus(0);
      SendMQMsg("deleteUser", user.getId());
      userMapper.updateById(user);
    }

    return RestResp.ok(true);
  }

  private void SendMQMsg(String method, Long userID) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(new SendMsgCodeToMqDto(method, new SendMsgCodeToMqParams(userID)));

      CorrelationData correlationData = new CorrelationData("msg-" + System.currentTimeMillis());
      // 手动构造 Message，避免 RabbitTemplate 二次序列化
      MessageProperties props = new MessageProperties();
      props.setCorrelationId(correlationData.getId());
      String key = (String) redisUtil.get(RabbitMQConstants.GOST_QUEUE_KEY);
      props.setHeader("x-api-key", key);
      Message message = new Message(json.getBytes(StandardCharsets.UTF_8), props); // 直接发送字节数组
      log.info("[SendMsgCodeToMq] 准备发送mq消息 {}", json);
      rabbitTemplate.send(RabbitMQConstants.GOST_EXCHANGE, "", message);

    } catch (JsonProcessingException e) {
      log.error("[SendMsgCodeToMq] 发送mq消息失败", e.getMessage());
    }

  }

  @Override
  public RestResp<User> createUser(@Valid UserCreateDto dto) {
    User user = createNewUser((UserDto) dto);

    if (user == null) {
      return RestResp.fail("创建失败");
    }
    return RestResp.ok(user);
  }

  @Override
  public RestResp<String> getSubscribe() {
    // 获取当前用户ID
    Long userID = Long.parseLong(StpUtil.getLoginId().toString());

    log.info("User getLoginId: {}", StpUtil.getLoginId());

    // 从数据库获取用户的 Link 信息，若不存在则生成新的 token
    Link tempLink = linkMapper.selectOne(new QueryWrapper<Link>().eq("user_id", userID));
    String token = Optional.ofNullable(tempLink)
        .map(Link::getToken)
        .orElseGet(() -> {
          String newToken = TokenGenerator.generateToken();
          Link newLink = new Link();
          newLink.setToken(newToken);
          newLink.setUserId(userID);
          linkMapper.insert(newLink);
          return newToken;
        });

    // 从配置中获取 subscribeHost
    String subscribeHost = configMapper.selectList(new QueryWrapper<Config>().eq("type", "subscribe"))
        .stream()
        .filter(config -> "subscribeHost".equals(config.getCode()))
        .map(Config::getValue)
        .findFirst()
        .orElse(""); // 默认值为空字符串

    // 构造最终的链接
    String finalLink = "https://" + subscribeHost + "/user/subscribe?token=" + token;
    return RestResp.ok(finalLink);
  }

  @Override
  public SubscribeRespDto generateSubscribe(String token) {
    try {
      Link linkItem = linkMapper.selectOne(new QueryWrapper<Link>().eq("token", token));
      if (linkItem == null) {
        throw new BusinessException("token不存在");
      }
      ForeignServerListReqDto reqdto = ForeignServerListReqDto.builder()
          .status(1) // 其他属性
          .build();
      reqdto.setFetchAll(true); // 手动设置 fetchAll.
      RestResp<PageRespDto<ForeignServerListRespDto>> response = foreignServerService.getForeignServerList(reqdto);

      ArrayList<ClashHttpProxy> clashHttpProxies = new ArrayList<ClashHttpProxy>();
      List<? extends ForeignServerListRespDto> records = new ArrayList<>();
      if (response.getCode() == 200 && response.getData() != null) {
        records = response.getData().getList();
      } else {
        log.info("[generateSubscribe] failed: {}", response.getMessage());
      }

      User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, linkItem.getUserId()));
      log.info("[generateSubscribe] 查询服务器记录条数: {}", records.size());
      for (ForeignServerListRespDto record : records) {
        ClashHttpProxy clashHttpProxy = ClashHttpProxy.builder()
            .name(record.getServerName())
            .server(record.getDomainName())
            // 不同套餐不同端口的？没必要，更改进程文件描述符限制即可
            .port(record.getPort())
            .username(Long.toString(user.getId()))
            .password(user.getPasswordHash())
            .build();

        clashHttpProxies.add(clashHttpProxy);
      }

      String yamlContent = YamlBuilder.buildYaml(clashHttpProxies);

      UsageRecord usageRecord = usageRecordMapper
          .selectOne(new LambdaQueryWrapper<UsageRecord>().eq(UsageRecord::getUserId, linkItem.getUserId())
              .eq(UsageRecord::getPurchaseStatus, 1));
      if (usageRecord == null) {
        throw new BusinessException("用户没有购买套餐");
      }
      Long expire = usageRecord.getPurchaseEndTime().toEpochSecond(ZoneOffset.UTC);
      String filename = configMapper
          .selectOne(new LambdaQueryWrapper<Config>().eq(Config::getCode, "filename").last("limit 1")).getValue();
      String webPagwUrl = configMapper
          .selectOne(new LambdaQueryWrapper<Config>().eq(Config::getCode, "webPagwUrl").last("limit 1")).getValue();

      return SubscribeRespDto.builder()
          .yamlContent(yamlContent)
          .expire(expire.toString())
          .consumedDataDownload(
              usageRecord.getConsumedDataDownload() != null ? usageRecord.getConsumedDataDownload().toString() : "0")
          .consumedDataUpload(
              usageRecord.getConsumedDataUpload() != null ? usageRecord.getConsumedDataUpload().toString() : "0")
          .consumedDataTransfer(
              usageRecord.getDataAllowance() != null ? usageRecord.getDataAllowance().toString() : "0")
          .filename(filename)
          .webPagwUrl(webPagwUrl)
          .build();

    } catch (Exception e) {
      log.error("[generateSubscribe] error: {}", e.getMessage());
      throw new BusinessException("生成订阅内容失败");
    }

  }
}
