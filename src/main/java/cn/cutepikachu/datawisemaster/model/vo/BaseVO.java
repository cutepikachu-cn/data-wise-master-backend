package cn.cutepikachu.datawisemaster.model.vo;

import cn.cutepikachu.datawisemaster.model.entity.BaseEntity;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public abstract class BaseVO<E extends BaseEntity<E, VO>, VO extends BaseVO<E, VO>> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public E toEntity(Class<E> eClass) {
        E entity = ReflectUtil.newInstance(eClass);
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
