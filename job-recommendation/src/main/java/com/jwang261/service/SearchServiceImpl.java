package com.jwang261.service;

import com.jwang261.dao.HistoryDao;
import com.jwang261.dao.ItemDao;
import com.jwang261.dao.KeywordDao;
import com.jwang261.external.GitHubClient;
import com.jwang261.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    HistoryDao historyDao;

    @Autowired
    ItemDao itemDao;

    @Autowired
    KeywordDao keywordDao;

    @Autowired
    GitHubClient gitHubClient;


    @Override
    public void setFavoriteItems(String userId, Item item) {
        this.saveItem(item);
        historyDao.setItem(item.getItemId(), userId);
    }

    @Override
    public void unsetFavoriteItems(String userId, String itemId) {
        historyDao.unsetItem(userId, itemId);
    }

    @Override
    public void saveItem(Item item) {
        itemDao.saveItem(item);
    }

    /**
     *
     * @param userId
     * @return list of itemIds
     */
    @Override
    public Set<String> getFavoriteItemIds(String userId) {

        return historyDao.selectItemIdByUserId(userId);
    }

    /**
     *
     * @param userId
     * @return list of items
     */
    @Override
    public Set<Item> getFavoriteItems(String userId) {
        Set<Item> items = new HashSet<>();
        Set<String> favoriteItemIds = this.getFavoriteItemIds(userId);
        for (String s : favoriteItemIds) {
            items.add(itemDao.selectItemByItemId(s));
        }
        return items;

    }

    /**
     *
     * @param itemId
     * @return items' keywords
     */
    @Override
    public Set<String> getKeywords(String itemId) {
        return keywordDao.selectKeywordByItemId(itemId);
    }


    @Override
    public List<Item> recommendItem(String userId, double lat, double lon) {
        List<Item> recommendedItems = new ArrayList<>();
        Set<String> favoriteItemIds = this.getFavoriteItemIds(userId);
        Map<String, Integer> allKeywords = new HashMap<>();
        for (String itemId : favoriteItemIds) {
            Set<String> keywords = keywordDao.selectKeywordByItemId(itemId);
            for (String keyword : keywords) {
                allKeywords.put(keyword,
                        allKeywords.getOrDefault(keyword, 0) + 1);
            }

        }
        Set<String> visitedItemsIds = new HashSet<>();
        Set<Map.Entry<String, Integer>> keywordSet = allKeywords.entrySet();
        List<Map.Entry<String, Integer>> keywordList = new ArrayList<>(keywordSet);
        Collections.sort(keywordList, (e1, e2) -> {
            return e1.getValue() - e2.getValue();
        });
        if(keywordList.size() > 3){
            keywordList = keywordList.subList(0, 3);
        }

        lon = -76;
        for (Map.Entry<String, Integer> entry : keywordList) {
            List<Item> items = gitHubClient.search(lat, lon, entry.getKey());
            for (Item item : items) {
                if(!favoriteItemIds.contains(item.getItemId())
                        && !visitedItemsIds.contains(item.getItemId())){
                    recommendedItems.add(item);
                    visitedItemsIds.add(item.getItemId());
                }
            }
        }
        return recommendedItems;
    }



}
