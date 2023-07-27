package com.emily.infrastructure.test.mapper.mysql;

import com.emily.infrastructure.test.po.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: spring-parent
 * 
 * @author Emily
 * @since 2021/09/08
 */
@Mapper
public interface ItemMapper {
    int inertByBatch(@Param("list") List<Item> list);

    int insertItem(@Param("scheName") String scheName, @Param("lockName") String lockName);
}
