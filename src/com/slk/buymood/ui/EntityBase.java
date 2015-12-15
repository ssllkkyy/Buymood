package com.slk.buymood.ui;

import com.lidroid.xutils.db.annotation.Id;

public abstract class EntityBase {
	
    //@NoAutoIncrement // int,long类型的id默认自增，不想使用自增时添加此注解
	 @Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
	private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
