package net.ipetty.core.util;

import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import net.ipetty.core.exception.BusinessException;

public class JavassistMethodParamNamesScaner {

	/**
	 * 获取方法参数名称
	 */
	protected static String[] getMethodParamNames(CtMethod ctMethod) {
		CtClass ctClass = ctMethod.getDeclaringClass();
		MethodInfo methodInfo = ctMethod.getMethodInfo();
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

		if (attr == null) {
			throw new BusinessException(ctClass.getName());
		}

		String[] paramNames = null;

		try {
			paramNames = new String[ctMethod.getParameterTypes().length];
		} catch (NotFoundException e) {
			throw new BusinessException(e);
		}
		int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
		for (int i = 0; i < paramNames.length; i++) {
			paramNames[i] = attr.variableName(i + pos);
		}
		return paramNames;
	}

	/**
	 * 获取方法参数名称，按给定的参数类型匹配方法
	 */
	public static String[] getMethodParamNames(Class<?> clazz, String method, Class<?>... paramTypes) {
		ClassPool pool = ClassPool.getDefault();
		CtClass ctClass = null;
		CtMethod ctMethod = null;

		try {
			ctClass = pool.get(clazz.getName());
			String[] paramTypeNames = new String[paramTypes.length];
			for (int i = 0; i < paramTypes.length; i++) {
				paramTypeNames[i] = paramTypes[i].getName();
			}
			ctMethod = ctClass.getDeclaredMethod(method, pool.get(paramTypeNames));
		} catch (NotFoundException e) {
			throw new BusinessException(e);
		}
		return getMethodParamNames(ctMethod);
	}

	/**
	 * 获取方法参数名称，匹配同名的某一个方法
	 * 
	 * @throws NotFoundException
	 *             如果类或者方法不存在
	 * @throws MissingLVException
	 *             如果最终编译的class文件不包含局部变量表信息
	 */
	public static String[] getMethodParamNames(Class<?> clazz, String method) {
		ClassPool pool = ClassPool.getDefault();
		CtClass ctClass;
		CtMethod ctMethod = null;
		try {
			ctClass = pool.get(clazz.getName());
			ctMethod = ctClass.getDeclaredMethod(method);
		} catch (NotFoundException e) {
			throw new BusinessException(e);
		}
		return getMethodParamNames(ctMethod);
	}

	public static String[] getMethodParamNames(Method method) {
		return getMethodParamNames(method.getDeclaringClass(), method.getName());
	}

}
