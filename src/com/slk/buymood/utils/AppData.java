package com.slk.buymood.utils;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.slk.buymood.ui.EntityBase;

/**全局数据状态
 * @author songlk
 *
 */
@Table(name = "appdata")  // 建议加上注解， 混淆后表名不受影响
public class AppData extends EntityBase{
	 @Column(column = "masterrole")
	public String masterRole;//buyer  seller  anonym
	 @Column(column = "islogin")
	 public boolean isLogin;   
	 @Column(column = "isregister")
	 public boolean isRegister;
	
	public String getMasterRole() {
		return masterRole;
	}
	public void setMasterRole(String masterRole) {
		this.masterRole = masterRole;
	}
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public boolean isRegister() {
		return isRegister;
	}
	public void setRegister(boolean isRegister) {
		this.isRegister = isRegister;
	}
	
}
