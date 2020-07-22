package com.ssafy.study.group.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.ssafy.study.group.model.Group;
import com.ssafy.study.group.model.GroupSearch;
import com.ssafy.study.group.model.QGroup;

@Repository
public class GroupRepositoryImpl  implements CustomGroupRepository {

	@Override
	public List<Group> searchGroups(GroupSearch groupSearch) {
		// TODO Auto-generated method stub
		return null;
	}

//	private QGroup group = QGroup.group;
//
//	public GroupRepositoryImpl() {
//		super(Group.class);
//	}
//
//	@Override
//	public List<Group> searchGroups(GroupSearch search) {
//		JPAQuery query = new JPAQuery(this.getEntityManager());
//
//		query.from(group).where(ckGpNm(search.getGpNm()));
//
//		return null;
//	}
//
//	private BooleanExpression ckGpNm(String nm) {
//		if (nm.equals("") || nm == null)
//			return null;
//		return group.gpNm.contains(nm);
//	}

}
