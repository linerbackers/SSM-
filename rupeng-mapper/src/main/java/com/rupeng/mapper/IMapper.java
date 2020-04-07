package com.rupeng.mapper;

import java.util.List;

public interface IMapper<T> {

    public int insert(T pojo);

    public int update(T pojo); //根据id更新

    public int delete(Long id); //根据id删除

    public List<T> select(T pojo); //以非空字段值作为查询条件进行查询
}
