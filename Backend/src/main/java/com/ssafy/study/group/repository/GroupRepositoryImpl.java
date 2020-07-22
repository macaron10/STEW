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
public class GroupRepositoryImpl extends QuerydslRepositorySupport implements CustomGroupRepository {

	public GroupRepositoryImpl() {
		super(Group.class);
	}

	@Override
	public List<Group> searchGroups(GroupSearch search) {
		JPAQuery query = new JPAQuery(this.getEntityManager());

		QGroup group = QGroup.group;
//		query.from(group).where(ckGpNm(search.getGpNm()));

		return null;
	}

//	private BooleanExpression ckGpNm(String nm) {
//		if (nm.equals("") || nm == null)
//			return null;
//		return group.gpNm.contains(nm);
//	}

}
