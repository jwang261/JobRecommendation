package com.jwang261.service;

import com.jwang261.pojo.Item;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SearchService {

    public void setFavoriteItems(String userId, Item item);

    public void unsetFavoriteItems(String userId, String itemId);

    public void saveItem(Item item);

    public Set<String> getFavoriteItemIds(String userId);

    public Set<Item> getFavoriteItems(String userId);

    public Set<String> getKeywords(String itemId);

    /**
     *
     * @param userId
     * @param lat
     * @param lon
     * @return items that contains top 3 keywords
     */
    public List<Item> recommendItem(String userId, double lat, double lon);


}
