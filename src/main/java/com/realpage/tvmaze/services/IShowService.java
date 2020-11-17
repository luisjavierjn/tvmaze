package com.realpage.tvmaze.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.realpage.tvmaze.domain.consts.OrderBy;
import com.realpage.tvmaze.domain.consts.Orientation;

import java.util.List;

public interface IShowService {

    ArrayNode getAllShows();

    ArrayNode applyFilters(List<String> filters, OrderBy ob, Orientation o);

    ArrayNode applyFilters(List<String> filters, OrderBy ob);

    ArrayNode applyFilters(List<String> filters);

    ArrayNode applyPaging(ArrayNode arrayNode, int amount, int page);
}
