/*
 * @Author: hasikiFire
 * @Date: 2024-06-04 11:13:28
 */
/*
 * @Author: hasikiFire
 * @Date: 2024-06-04 11:13:28
 */
package com.hasikiFire.networkmall.service.impl;

import com.hasikiFire.networkmall.core.common.constant.DatabaseConsts;
import com.hasikiFire.networkmall.core.common.constant.DatabaseConsts.RolesTable.RoleEnum;
import com.hasikiFire.networkmall.core.common.constant.SendCodeTypeEnum;
import com.hasikiFire.networkmall.core.common.exception.BusinessException;
import com.hasikiFire.networkmall.core.common.req.UserDto;
import com.hasikiFire.networkmall.core.common.resp.PageRespDto;
import com.hasikiFire.networkmall.core.common.resp.RestResp;
import com.hasikiFire.networkmall.core.util.PasswordUtils;
import com.hasikiFire.networkmall.core.util.RedisUtil;
import com.hasikiFire.networkmall.dao.entity.Roles;
import com.hasikiFire.networkmall.dao.entity.UsageRecord;
import com.hasikiFire.networkmall.dao.entity.User;
import com.hasikiFire.networkmall.dao.entity.UserId;
import com.hasikiFire.networkmall.dao.entity.Wallet;
import com.hasikiFire.networkmall.dao.mapper.RolesMapper;
import com.hasikiFire.networkmall.dao.mapper.UsageRecordMapper;
import com.hasikiFire.networkmall.dao.mapper.UserIdMapper;
import com.hasikiFire.networkmall.dao.mapper.UserMapper;
import com.hasikiFire.networkmall.dao.mapper.WalletMapper;
import com.hasikiFire.networkmall.dto.req.UserCreateDto;
import com.hasikiFire.networkmall.dto.req.UserEditDto;
import com.hasikiFire.networkmall.dto.req.UserListReqDto;
import com.hasikiFire.networkmall.dto.req.UserLoginReqDto;
import com.hasikiFire.networkmall.dto.req.UserRegisterReqDto;
import com.hasikiFire.networkmall.dto.req.UsersendEmailCodeDto;
import com.hasikiFire.networkmall.dto.resp.UserInfoRespDto;
import com.hasikiFire.networkmall.dto.resp.UserListRespDto;
import com.hasikiFire.networkmall.dto.resp.UserLoginRespDto;
import com.hasikiFire.networkmall.dto.resp.UserRegisterRespDto;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
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
 * @author ${author}
 * @since 2024/06/03
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  @Value("${spring.mail.username}")
  private String username;

  private final UserMapper userMapper;

  private final UserIdMapper userIDMapper;

  private final WalletMapper walletMapper;
  private final RolesMapper roleMapper;
  private final UsageRecordMapper usageRecordMapper;

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
      throw new BusinessException("验证码错误");
    }

    User user = createNewUser((UserDto) dto);
    // 删除验证码
    redisUtil.delete(redisKey);
    Roles role = new Roles();
    role.setUserId(user.getUserId());
    role.setRoleName(RoleEnum.USER.getRoleName());
    roleMapper.insert(role);

    StpUtil.login(user.getUserId());
    String token = StpUtil.getTokenValue();
    // 生成JWT 并返回
    return RestResp.ok(
        UserRegisterRespDto.builder()
            .token(token)
            .uid(user.getUserId())
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
      throw new BusinessException("邮箱或者密码错误");
    }
    String passwordHash = DigestUtils.md5DigestAsHex(
        (dto.getPassword() + user.getSalt()).getBytes(StandardCharsets.UTF_8));

    if (!passwordHash.equals(user.getPasswordHash())) {
      throw new BusinessException("邮箱或者密码错误");
    }

    StpUtil.login(user.getUserId());
    String token = StpUtil.getTokenValue();

    return RestResp.ok(
        UserLoginRespDto.builder()
            .token(token)
            .uid(user.getUserId())
            .build());

  }

  @Override
  public RestResp<UserInfoRespDto> getUserInfo(Long id) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERID,
        id)
        .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
    User user = userMapper.selectOne(queryWrapper);
    if (user == null) {
      throw new BusinessException("用户不存在");
    }
    return RestResp.ok(
        UserInfoRespDto.builder()
            .userId(user.getUserId())
            .name(user.getName())
            .email(user.getEmail())
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
      message.setText("本次" + emailDto.getType().getDesc() + "为: " + code + " ，有效期为 10 分钟");
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
      queryWrapper.eq(User::getUserId, params.getUserId());
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
    List<Long> userIds = users.stream().map(User::getUserId).collect(Collectors.toList());
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
      Long userId = user.getUserId();
      Wallet userWallet = userIdToWalletMap.get(userId); // 假设每个用户只有一个wallet
      List<UsageRecord> userPackageRecords = userIdToPackageRecordsMap.getOrDefault(userId,
          new ArrayList<>());

      return UserListRespDto.builder()
          .userId(user.getUserId())
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
      queryWrapper.eq(User::getUserId, dto.getUserId());
      user = userMapper.selectOne(queryWrapper);
      log.info("User: selectOne {}", user);
      if (user == null) {
        throw new BusinessException("用户不存在");
      } else {
        user = updateNewUser(dto, user);
      }
    }

    if (user == null) {
      throw new BusinessException("更新失败");
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
    // 随机取一个未使用的用户ID
    LambdaQueryWrapper<UserId> queryWrapperUserID = new LambdaQueryWrapper<>();
    queryWrapperUserID.eq(UserId::getUserId, newUserDtoser.getUserId());
    UserId userIdModal = userIDMapper.selectOne(queryWrapperUserID);
    if (userIdModal != null) {
      userIdModal.setStatus(1);
      userIDMapper.updateById(userIdModal);
    } else {
      log.info("用户userID 未在user_id 数据库里", userIdModal);
    }

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
    // 随机取一个未使用的用户ID
    QueryWrapper<UserId> queryWrapperUserID = new QueryWrapper<>();
    queryWrapperUserID.eq("status", 0).orderByAsc("RAND()").last("LIMIT 1");
    // TODO uuid
    // UserId userIdModal = userIDMapper.selectOne(queryWrapperUserID);
    // if (userIdModal == null) {
    // throw new BusinessException("用户ID已用完");
    // }
    // userIdModal.setStatus(1);
    // userIDMapper.updateById(userIdModal);
    // Long userId = userIdModal.getUserId();
    // 注册成功，保存用户信息
    User user = new User();
    if (dto.getPassword() == null) {
      throw new BusinessException("密码不能为空");
    }
    if (dto.getEmail() == null) {
      throw new BusinessException("邮箱不能为空");
    }
    if (dto.getName() == null) {
      throw new BusinessException("名字不能为空");
    }
    String salt = PasswordUtils.generateSalt();
    String passwordHash = DigestUtils.md5DigestAsHex(
        (dto.getPassword() + salt).getBytes(StandardCharsets.UTF_8));
    user.setPasswordHash(
        passwordHash);
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());
    // user.setUserId(userId);
    user.setStatus(1);
    user.setSalt(salt);
    userMapper.insert(user);
    return user;
  }

  @Override
  public RestResp<String> deleteUser(Integer status) {
    return null;
    // 在这里实现 deleteUser 方法
  }

  @Override
  public RestResp<User> createUser(@Valid UserCreateDto dto) {
    User user = createNewUser((UserDto) dto);

    if (user == null) {
      throw new BusinessException("创建失败");
    }
    return RestResp.ok(user);
  }
}
