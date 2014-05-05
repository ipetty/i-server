package net.ipetty.user.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ipetty.core.domain.AbstractEntity;
import net.ipetty.pet.domain.Pet;

/**
 * 用户
 * 
 * @author luocanfeng
 * @date 2014年4月29日
 */
public class User extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 5372132560605135165L;

	private Integer id;
	private Date createdOn; // 创建/注册时间

	private int uid; // uid，用户不可见，在用户未设置爱宠号时有用
	private String account; // 爱宠号，用户在爱宠的唯一标识，一经设定不能更改，可作登录帐号
	private String phoneNumber; // 手机号，绑定手机号需短信验证，可作登录帐号
	private String email; // 邮箱地址，绑定邮箱需邮件验证，可作登录帐号
	private String qq; // QQ号，通过第三方帐号登录后自动绑定，可作登录帐号
	private String qzoneUid; // 通过QQ空间（第三方帐号）登录的用户的uid
	private String weiboAccount; // 新浪微博帐号，通过第三方帐号登录后自动绑定，可作登录帐号
	private String weiboUid; // 通过新浪微博（第三方帐号）登录的用户的uid
	private String password; // 密码
	private String encodedPassword; // 加密后的密码
	private String salt; // 密码盐值

	private UserProfile profile; // 个人资料
	private UserZone zone; // 个人空间
	private UserPreferences preferences; // 个人设置
	private UserStatistics statistics; // 统计信息

	private List<Pet> pets = new ArrayList<Pet>(); // 宠物

	private List<Role> roles = new ArrayList<Role>(); // 拥有的角色

	public void prePersist() {
		this.setCreatedOn(new Date());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getQzoneUid() {
		return qzoneUid;
	}

	public void setQzoneUid(String qzoneUid) {
		this.qzoneUid = qzoneUid;
	}

	public String getWeiboAccount() {
		return weiboAccount;
	}

	public void setWeiboAccount(String weiboAccount) {
		this.weiboAccount = weiboAccount;
	}

	public String getWeiboUid() {
		return weiboUid;
	}

	public void setWeiboUid(String weiboUid) {
		this.weiboUid = weiboUid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncodedPassword() {
		return encodedPassword;
	}

	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public UserProfile getProfile() {
		return profile;
	}

	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	public UserZone getZone() {
		return zone;
	}

	public void setZone(UserZone zone) {
		this.zone = zone;
	}

	public UserPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(UserPreferences preferences) {
		this.preferences = preferences;
	}

	public UserStatistics getStatistics() {
		return statistics;
	}

	public void setStatistics(UserStatistics statistics) {
		this.statistics = statistics;
	}

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
