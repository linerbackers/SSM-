package com.rupeng.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.rupeng.pojo.Card;
import com.rupeng.pojo.CardSubject;
import com.rupeng.pojo.Subject;

@Service
public class CardSubjectService extends ManyToManyBaseService<Card, CardSubject, Subject>{
	
	public void order(Long[] cardSubjectIds,Integer[] seqNums){
		for(int i=0;i<cardSubjectIds.length;i++){
			CardSubject cs=new CardSubject();
			cs.setId(cardSubjectIds[i]);
			CardSubject selectOne = selectOne(cs);
			selectOne.setSeqNum(seqNums[i]);
			update(selectOne);
		}
	}

	public List<Card> selectFirstListBySecondId(Long subjectId, String string) {
		PageHelper.orderBy(string);
		return	selectFirstListBySecondId(subjectId);
	}

/*	public void updateCard(Long cardId,Long[] secondIds){
		//1、查询出cardid的学习卡所属的所有学科
		CardSubject cardSubject=new CardSubject();
		cardSubject.setId(cardId);
		List<CardSubject> oldList = selectList(cardSubject);
		
		//为了方便 把long[] secondIds数组转换成list
		List<Long> secondList=new ArrayList<>();
		for(Long ids:secondIds){
			if(ids!=null){
				secondList.add(ids);
			}
		}
		
		//2、分析情况  用户可选择java 和.net
		if(oldList!=null){
			for(CardSubject cardSub:oldList){
				if(secondList.contains(cardSub.getSubjectId())){
					//1、比如原来这张学习卡属于java ,现在用户还是把java打钩，我们就不做删除等任何操作
				}else{
					//2、原来的这张学习卡不属于java, 现在用户取消掉java,则在数据库中把这条记录删除
					delete(cardSub.getId());
				}
				//3、既属于java,又属于其他，需要删除sceondList里面存放java的旧Id.数据库中已经存放java了，我们没必要存第二次。
				secondList.remove(cardSub.getSubjectId());
			}
		}
		//最后把用户新勾选的学科id添加进数据库
		for(Long id:secondList){
			CardSubject cs=new CardSubject();
			cs.setSubjectId(id);
			cs.setCardId(cardId);
			insert(cs);
		}
	}*/
}
