package com.rupeng.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rupeng.mapper.IMapper;
/**
 * 系统异常最好不要直接抛出runtimexception 因为runtimeexception可以不使用try...catch进行处理，但是如果有异常
 * 则异常由JVM进行处理，且程序中断执行。为了保证程序出错后依然可以执行，在开发代码的时候最好使用try...catch的异常机制处理
 * @author Administrator
 *
 * @param <T>
 */
public class BaseService<T>{

	//可以自动根据泛型T的具体值注入对应的mapper！！！
	//如果T是Subject，那么注入的mapper就是SubjectMapper的对象
	@Autowired
	private IMapper<T> mapper;
	
	@SuppressWarnings("all")
	private T createInstanceAndSetId(Long id){
		if(id==null){
			throw new  RuntimeException("id不能为空！");
		}
		//1、获得它父类的泛型对象即 BaseService<T> ,因为此处传进来的T是子类，所以getclass()也是子类。
		Type type = getClass().getGenericSuperclass();
		//2、获得运行时T真实类型
		ParameterizedType t=(ParameterizedType)type;//转换成参数化泛型
		Type[] actualTypeArguments = t.getActualTypeArguments();//比如传进来的T只有一个subject,数组中即存放一个
		Class tclass = (Class) actualTypeArguments[0];
		T instance=null;
		try {
			instance = (T) tclass.newInstance();//例如subject类
			tclass.getDeclaredMethod("setId",Long.class ).invoke(instance, id);
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		} catch (InstantiationException e) {
		}
		
		return instance;
	}
	
	public int insert(T t){
		return mapper.insert(t);
	}
	
	public int update(T t){
		return mapper.update(t);
	}
	
	public int delete(Long id){
		return mapper.delete(id);
	}
	
	public T selectOne(T t){
		 List<T> list = mapper.select(t);
		 if(list.size()==0||list.size()>1){
			 throw new RuntimeException("查出数据超过一条以上！");
		 }
		 return list.get(0);
		 
	}
	
	public List<T> selectList(){
		return mapper.select(null);
	}
	
	public List<T> selectList(T t){
		return mapper.select(t);
	}
	
	/**
	 * 条件排序查询
	 * @return
	 */
	public List<T> selectList(T t, String orderBy){
		PageHelper.orderBy(orderBy);
		return mapper.select(t);
	} 
	
	/**
	 * 根据id进行查询
	 * @param id
	 * @return
	 */
	public T selectOne(Long id){
		T t = createInstanceAndSetId(id);
		return selectOne(t);
	}
	
	    public PageInfo<T> page(int pageNum, int pageSize, T pojo) {
	        PageHelper.startPage(pageNum, pageSize);//注意pageNum表示页码，从1开始
	        List<T> list = mapper.select(pojo);//正常执行自己的Mapper的查询方法
	        return new PageInfo<T>(list);
	    }

	    public PageInfo<T> page(int pageNum, int pageSize, T pojo, String orderBy) {
	        PageHelper.startPage(pageNum, pageSize);//注意pageNum表示页码，从1开始
	        PageHelper.orderBy(orderBy);
	        List<T> list = mapper.select(pojo);//正常执行自己的Mapper的查询方法
	        return new PageInfo<T>(list);
	    }
	
	/**
	 *  判断是否存在
	 * @param pojo
	 * @return
	 */
	public boolean isExisted(T pojo){
		List<T> selectOne = selectList(pojo);
		if(selectOne.size()>=1){
			return false;
		}
		return true;
	}
}
