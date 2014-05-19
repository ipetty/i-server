package net.ipetty.core.cache;


/**
 * HazelcastCacheInterceptor
 * 
 * @author luocanfeng
 * @date 2014年5月15日
 */
// @Component
// @Aspect
public class HazelcastCacheInterceptor {

	// private final String GET = "@annotation(LoadFromHazelcast)";
	// private final String SET = "@annotation(UpdateToHazelcast)";
	//
	// private Logger log = LoggerFactory.getLogger(getClass());
	//
	// /**
	// * 首先从缓存中加载数据，缓存命中则返回数据，未命中则从数据库查找，并加入缓存
	// */
	// @Around(GET)
	// public Object get(ProceedingJoinPoint call) throws Throwable {
	// LoadFromHazelcast annotation = getAnnotation(call,
	// LoadFromHazelcast.class);
	// String key = annotation.value();
	// int timeSocpe = annotation.timeScope();
	//
	// if (!executeCondition(annotation.condition(), call)) { //
	// 不满足条件,直接调用方法，不进行缓存AOP操作
	// return call.proceed();
	// }
	//
	// key = getKeyNameFromParam(key, call);
	//
	// Object value = null;
	//
	// try {
	// value = cache.get(key);
	// } catch (TimeoutException e) {
	// log.error("Get Data From Memcached TimeOut!About Key:" + key, e);
	// e.printStackTrace();
	// } catch (InterruptedException e) {
	// log.error("Get Data From Memcached TimeOut And Interrupted!About Key:" +
	// key, e);
	// e.printStackTrace();
	// } catch (MemcachedException e) {
	// log.error("Get Data From Memcached And Happend A Unexpected Error!About Key:"
	// + key, e);
	// e.printStackTrace();
	// }
	//
	// if (value == null) {
	// value = call.proceed();
	// if (value != null) {
	// try {
	// cache.add(key, timeSocpe, value);
	// log.info("Add Data For Memcached Success!About Key:" + key);
	// } catch (TimeoutException e) {
	// log.error("Add Data For Memcached TimeOut!About Key:" + key, e);
	// e.printStackTrace();
	// } catch (InterruptedException e) {
	// log.error("Add Data For Memcached TimeOut And Interrupted!About Key:" +
	// key, e);
	// e.printStackTrace();
	// } catch (MemcachedException e) {
	// log.error("Add Data For Memcached And Happend A Unexpected Error!About Key:"
	// + key, e);
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// return value;
	// }
	//
	// /**
	// * 执行方法的同时更新缓存中的数据
	// */
	// @Around(SET)
	// public Object update(ProceedingJoinPoint call) throws Throwable {
	// UpdateToHazelcast annotation = getAnnotation(call,
	// UpdateToHazelcast.class);
	// String[] key = annotation.value();// 可能需要更新多个key
	//
	// Object value = call.proceed();
	// if (!executeCondition(annotation.condition(), call)) {//
	// 不满足条件,直接调用方法，不进行缓存AOP操作
	// return value;
	// }
	//
	// if (value != null) {
	// try {
	// for (String singleKey : key) {// 循环处理所有需要更新的key
	// String tempKey = getKeyNameFromParam(singleKey, call);
	// cache.delete(tempKey);
	// }
	// log.info("Update Data For Memcached Success!About Key:" + key);
	// } catch (TimeoutException e) {
	// log.error("Update Data For Memcached TimeOut!About Key:" + key, e);
	// e.printStackTrace();
	// } catch (InterruptedException e) {
	// log.error("Update Data For Memcached TimeOut And Interrupted!About Key:"
	// + key, e);
	// e.printStackTrace();
	// } catch (MemcachedException e) {
	// log.error("Update Data For Memcached And Happend A Unexpected Error!About Key:"
	// + key, e);
	// e.printStackTrace();
	// }
	//
	// }
	// return value;
	// }
	//
	// /**
	// * 获得Annotation对象
	// */
	// private <T extends Annotation> T getAnnotation(ProceedingJoinPoint jp,
	// Class<T> clazz) {
	// MethodSignature joinPointObject = (MethodSignature) jp.getSignature();
	// Method method = joinPointObject.getMethod();
	// return method.getAnnotation(clazz);
	// }
	//
	// /**
	// * @Title: getKeyNameFromParam
	// * @Description: 获得组合后的KEY值
	// * @param @param key
	// * @param @param jp
	// * @param @return
	// * @return String
	// * @throws
	// */
	// private String getKeyNameFromParam(String key, ProceedingJoinPoint jp) {
	// if (!key.contains("$")) {
	// return key;
	// }
	//
	// String regexp = "\\$\\{[^\\}]+\\}";
	// Pattern pattern = Pattern.compile(regexp);
	// Matcher matcher = pattern.matcher(key);
	// List<String> names = new ArrayList<String>();
	// try {
	// while (matcher.find()) {
	// names.add(matcher.group());
	// }
	// key = executeNames(key, names, jp);
	// } catch (Exception e) {
	// log.error("Regex Parse Error!", e);
	// }
	//
	// return key;
	// }
	//
	// /**
	// * @Title: executeNames
	// * @Description: 对KEY中的参数进行替换
	// * @param @param key
	// * @param @param names
	// * @param @param jp
	// * @param @return
	// * @param @throws OgnlException
	// * @return String
	// * @throws
	// */
	// private String executeNames(String key, List<String> names,
	// ProceedingJoinPoint jp) throws OgnlException {
	//
	// Method method = ((MethodSignature) jp.getSignature()).getMethod();
	//
	// // 形参列表
	// List<String> param = MethodParamNamesScaner.getParamNames(method);
	//
	// if (names == null || names.size() == 0) {
	// return key;
	// }
	//
	// Object[] params = jp.getArgs();
	//
	// Map<String, Object> map = new HashMap<String, Object>();
	// for (int i = 0; i < param.size(); i++) {
	// map.put(param.get(i), params[i]);
	// }
	//
	// for (String name : names) {
	// String temp = name.substring(2);
	// temp = temp.substring(0, temp.length() - 1);
	// key = myReplace(key, name, (String) Ognl.getValue(temp, map));
	// }
	//
	// return key;
	// }
	//
	// /**
	// * @Title: myReplace
	// * @Description: 不依赖Regex的替换，避免$符号、{}等在String.replaceAll方法中当做Regex处理时候的问题。
	// * @param @param src
	// * @param @param from
	// * @param @param to
	// * @param @return
	// * @return String
	// * @throws
	// */
	// private String myReplace(String src, String from, String to) {
	// int index = src.indexOf(from);
	// if (index == -1) {
	// return src;
	// }
	//
	// return src.substring(0, index) + to + src.substring(index +
	// from.length());
	// }
	//
	// /**
	// * @Title: executeCondition
	// * @Description: 判断是否需要进行缓存操作
	// * @param @param condition parm
	// * @param @return
	// * @return boolean true:需要 false：不需要
	// * @throws
	// */
	// private boolean executeCondition(String condition, ProceedingJoinPoint
	// jp) {
	//
	// if ("".equals(condition)) {
	// return true;
	// }
	//
	// Method method = ((MethodSignature) jp.getSignature()).getMethod();
	//
	// // 形参列表
	// List<String> param = MethodParamNamesScaner.getParamNames(method);
	//
	// if (param == null || param.size() == 0) {
	// return true;
	// }
	//
	// Object[] params = jp.getArgs();
	//
	// Map<String, Object> map = new HashMap<String, Object>();
	// for (int i = 0; i < param.size(); i++) {
	// map.put(param.get(i), params[i]);
	// }
	// boolean returnVal = false;
	// try {
	// returnVal = (Boolean) Ognl.getValue(condition, map);
	// } catch (OgnlException e) {
	// e.printStackTrace();
	// }
	//
	// return returnVal;
	// }
	//
	// public void setCache(MemcachedClient cache) {
	// this.cache = cache;
	// }

}
