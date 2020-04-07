package com.rupeng.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.rupeng.mapper.IManyToManyMapper;

public class ManyToManyBaseService<F,T,S> extends BaseService<T>{

	@Autowired
	private IManyToManyMapper<F, T, S> manyToManyMapper;
	
	@SuppressWarnings("all")
	public void updateFirst(Long firstId, Long[] secondIds){
		ParameterizedType Superclass = (ParameterizedType) getClass().getGenericSuperclass();
		Type[] actualTypeArguments = Superclass.getActualTypeArguments();
		Class fclass = (Class) actualTypeArguments[0];
		Class tclass = (Class) actualTypeArguments[1];
		Class sclass = (Class) actualTypeArguments[2];
		
		//例如cardsubject 
		//1、查询出cardid的学习卡所属的所有学科
		try {
			T t = (T) tclass.newInstance();
			//获取并调用T的set(First)Id方法
			tclass.getDeclaredMethod("set"+fclass.getSimpleName()+"Id", Long.class).invoke(t, firstId);
			List<T> oldList = selectList(t);
			
			//为了方便 把long[] secondIds数组转换成list
			List<Long> secondList=new ArrayList<>();
			for(Long id:secondIds){
				if(id!=null){
					secondList.add(id);
				}
			}
			
			//2、分析情况  用户可选择java 和.net
			for(T c:oldList){
				//拿到c对象的secondId
				Long sId=(Long) tclass.getDeclaredMethod("get"+sclass.getSimpleName()+"Id").invoke(c);
				if(secondList.contains(sId)){
					
				}else{
					delete((Long) tclass.getDeclaredMethod("getId").invoke(c));
				}
				secondList.remove(sId);
			}
			
			//最后把用户新勾选的学科id添加进数据库
			for(Long id2:secondIds){
				tclass.getDeclaredMethod("set"+sclass.getSimpleName()+"Id", Long.class).invoke(t, id2);
				tclass.getDeclaredMethod("set"+fclass.getSimpleName()+"Id", Long.class).invoke(t, firstId);
				insert(t);
			}
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
	} 
	
	@SuppressWarnings("all")
	public void updateSecond(Long secondId, Long[] firstIds){
		
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
		Class fclass = (Class) actualTypeArguments[0];
		Class tclass = (Class) actualTypeArguments[1];
		Class sclass = (Class) actualTypeArguments[2];
		
		
		try {
			//1、查询数据库
			T t = (T) tclass.newInstance();
			tclass.getDeclaredMethod("set"+sclass.getSimpleName()+"Id", Long.class).invoke(t, secondId);
			List<T> oldList = selectList(t);
			
			//把数组中的数据转移到list中（方便下面的操作），并且把无效数据（firstId==null）剔除
			List<Long> firstList=new ArrayList<>();
			for(Long fid:firstIds){
				if(fid!=null){
					firstList.add(fid);
				}
			}
			
 			// 2、删掉原本在数据库中但现在不在firstIdList中的数据
			for(T ot:oldList){
				Long otId = (Long) tclass.getDeclaredMethod("get"+fclass.getSimpleName()+"Id").invoke(ot);
				if(firstList.contains(otId)){
					
				}else{
					delete((Long) tclass.getDeclaredMethod("getId").invoke(ot));
				}
				firstList.remove(otId);
			}
			
			//3、 把firstIdList中剩余的添加到数据库
			for(Long fIdList:firstList){
				tclass.getDeclaredMethod("set"+fclass.getSimpleName()+"Id",Long.class).invoke(t, firstIds);
				tclass.getDeclaredMethod("set"+sclass.getSimpleName()+"Id",Long.class).invoke(t, secondId);
				insert(t);
			}
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
		
	}
	
	public int deleteByFirstId(Long firstId){
		if(firstId==null){
			throw new RuntimeException("firstId不能为null");
		}
		return manyToManyMapper.deleteByFirstId(firstId);
	}
	
	public int deleteBySecondId(Long secondId){
		if(secondId==null){
			throw new RuntimeException("secondId不能为null");
		}
		return manyToManyMapper.deleteBySecondId(secondId);
	}
	
	public List<F> selectFirstListBySecondId(Long secondId){
		List<F> list = manyToManyMapper.selectFirstListBySecondId(secondId);
		
		if(list.size()==0||list==null){
			return null;
		}
		return list;
	}
	
	public F selectFirstOneBySecondId(Long secondId){
		List<F> list = selectFirstListBySecondId(secondId);
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0);
	}
	public List<S> selectSecondListByFirstId(Long firstId){
		 List<S> list = manyToManyMapper.selectSecondListByFirstId(firstId);
		 if(list==null||list.size()==0){
			 return null;
		 }
		 return list;
	}
	
	public S selectSecondOneByFirstId(Long firstId){
		List<S> list = selectSecondListByFirstId(firstId);
		if(list==null||list.size()==0){
			return null;
		}
		return list.get(0);
	}
}
