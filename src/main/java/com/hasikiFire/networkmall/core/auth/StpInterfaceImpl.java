package com.hasikiFire.networkmall.core.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hasikiFire.networkmall.dao.entity.Roles;
import com.hasikiFire.networkmall.dao.mapper.RolesMapper;
import cn.dev33.satoken.stp.StpInterface;
import lombok.RequiredArgsConstructor;

/**
 * 自定义权限加载接口实现类
 */
@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {
  private final RolesMapper rolesMapper;

  /**
   * 返回一个账号所拥有的权限码集合
   */
  // @Override
  // public List<String> getPermissionList(Object loginId, String loginType) {
  // // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询权限
  // List<String> list = new ArrayList<String>();
  // list.add("101");
  // list.add("user.add");
  // list.add("user.update");
  // list.add("user.get");
  // // list.add("user.delete");
  // list.add("art.*");
  // return list;
  // }

  /**
   * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
   */
  @Override
  public List<String> getRoleList(Object loginId, String loginType) {
    Roles roles = rolesMapper.selectOne(new LambdaQueryWrapper<Roles>().eq(Roles::getUserId, loginId));
    List<String> list = new ArrayList<String>();
    list.add(roles.getRoleName());
    return list;
  }

  @Override
  public List<String> getPermissionList(Object arg0, String arg1) {
    List<String> list = new ArrayList<String>();
    return list;
  }

}
