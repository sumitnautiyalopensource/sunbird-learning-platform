package org.ekstep.language.Util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ekstep.language.model.LanguageSynsetData;
import org.ekstep.language.model.SynsetData;
import org.ekstep.language.model.SynsetDataLite;
import org.ekstep.language.util.WordUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.google.common.base.Charsets;

public class IndowordnetUtil {

	@SuppressWarnings({ "unchecked" })
	public void loadWords(String language, int offset, int limit) {
		Session session = HibernateSessionFactory.getSession();
		String languageTableName = getLanguageTableName(language);
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Query query = session.createQuery("FROM "+ languageTableName+" ORDER BY synset_id");
			query.setFirstResult(offset);
			query.setMaxResults(limit);
			
			List<LanguageSynsetData> languageSynsetDataList = query.list();
			 for (LanguageSynsetData lSynsetData : languageSynsetDataList){
				SynsetData synsetData = lSynsetData.getSynsetData();
				Map<String, Object> wordRequestMap = getWordMap(synsetData);
			}
			
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			HibernateSessionFactory.closeSession();
		}
	}

	private Map<String, Object> getWordMap(SynsetData synsetData) {
		byte[] bSynset = synsetData.getSynset();
    	String synsetString = new String(bSynset,Charsets.UTF_8);
    	String[] words = synsetString.split(",");
    	
    	for(String word: words){
    		
    	}
    	
        System.out.println("Word: " + synsetString);
        for(SynsetDataLite hypernym: synsetData.getHypernyms()){
        	bSynset = hypernym.getSynset();
        	String hsynsetString = new String(bSynset,Charsets.UTF_8);
            System.out.println("Hypernym: " + hsynsetString);
        }
        for(SynsetDataLite hyponym: synsetData.getHyponyms()){
        	bSynset = hyponym.getSynset();
        	String hysynsetString = new String(bSynset,Charsets.UTF_8);
            System.out.println("Hyponym: " + hysynsetString);
        }
        for(SynsetDataLite holonym: synsetData.getHolonyms()){
        	bSynset = holonym.getSynset();
        	String hysynsetString = new String(bSynset,Charsets.UTF_8);
            System.out.println("Holonym: " + hysynsetString);
        }
        for(SynsetDataLite meronym: synsetData.getMeronyms()){
        	bSynset = meronym.getSynset();
        	String hysynsetString = new String(bSynset,Charsets.UTF_8);
            System.out.println("Meronym: " + hysynsetString);
        }
        for(SynsetDataLite antonym: synsetData.getAntonyms()){
        	bSynset = antonym.getSynset();
        	String hysynsetString = new String(bSynset,Charsets.UTF_8);
            System.out.println("Antonym: " + hysynsetString);
        }
        for(SynsetDataLite actionObject: synsetData.getActionObjects()){
        	bSynset = actionObject.getSynset();
        	String hysynsetString = new String(bSynset,Charsets.UTF_8);
            System.out.println("Object: " + hysynsetString);
        }
        for(SynsetDataLite action: synsetData.getActions()){
        	bSynset = action.getSynset();
        	String hysynsetString = new String(bSynset,Charsets.UTF_8);
            System.out.println("Action : " + hysynsetString);
        }
        
        for(Map.Entry<String, SynsetDataLite> entry: synsetData.getTranslations().entrySet()){
        	String translatedLanguage = entry.getKey();
        	SynsetDataLite translatedSynsetDataLite = entry.getValue();
        	bSynset = translatedSynsetDataLite.getSynset();
        	String hysynsetString = new String(bSynset,Charsets.UTF_8);
        	System.out.println(translatedLanguage + " Translation: " + hysynsetString);
        }
		return null;
	}

	public static void main(String[] args) {
		IndowordnetUtil util = new IndowordnetUtil();
		util.loadWords("tamil", 0, 300);
	}
	
	private String getLanguageTableName(String language) {
		language = StringUtils.capitalize(language.toLowerCase());
		String tableName = language + IndowordnetConstants.SynsetData.name();
		return tableName;
	}
}
