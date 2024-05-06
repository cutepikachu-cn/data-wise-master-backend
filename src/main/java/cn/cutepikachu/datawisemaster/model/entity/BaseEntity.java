package cn.cutepikachu.datawisemaster.model.entity;

import cn.cutepikachu.datawisemaster.model.vo.BaseVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
public abstract class BaseEntity<E extends BaseEntity<E, VO>, VO extends BaseVO<E, VO>> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public VO toVO(Class<VO> voClass) {
        VO vo = ReflectUtil.newInstance(voClass);
        BeanUtil.copyProperties(this, vo);
        return vo;
    }
}
