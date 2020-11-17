package com.realpage.tvmaze.controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.realpage.tvmaze.domain.consts.OrderBy;
import com.realpage.tvmaze.domain.consts.Orientation;
import com.realpage.tvmaze.domain.dto.ApiResponse;
import com.realpage.tvmaze.exceptions.DateFormatInvalidException;
import com.realpage.tvmaze.exceptions.OrderTypeInvalidException;
import com.realpage.tvmaze.exceptions.PageIndexNotFoundException;
import com.realpage.tvmaze.exceptions.PagingDataInvalidException;
import com.realpage.tvmaze.services.IShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@RestController
@RequestMapping("/shows")
public class ShowController {

    public static final String INFO_RETRIEVED_SUCCESSFULLY = "Info retrieved successfully";

    @Value("${tvmaze.url.show.by.id}")
    private String urlShowById;

    @Value("${tvmaze.url.episode.by.date}")
    private String urlEpisodeByDate;

    @Autowired
    private IShowService showService;

    @GetMapping(value = "/all", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ApiResponse<ArrayNode> getAllShows() {
        return new ApiResponse<>(HttpStatus.OK.value(),INFO_RETRIEVED_SUCCESSFULLY,showService.getAllShows());
    }

    @GetMapping(value = "/qp/{amount}/{page}/{orderby}/{orientation}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ApiResponse<ArrayNode> getPagedShowsByOrderByAndOrientation(@PathVariable int amount,
                                                                       @PathVariable int page,
                                                                       @PathVariable OrderBy orderby,
                                                                       @PathVariable Orientation orientation,
                                                                       @RequestParam("filter") List<String> filters) {
        if(orderby != OrderBy.NONE) {
            ArrayNode filteredOnes = showService.applyFilters(filters, orderby, orientation);
            Optional<ArrayNode> pagedAndFilteredOnes;
            if(amount > 0 && page >= 0) {
                pagedAndFilteredOnes = Optional.ofNullable(showService.applyPaging(filteredOnes, amount, page));
                if(!pagedAndFilteredOnes.isPresent()) {
                    throw new PageIndexNotFoundException("page required is out of scope");
                }
            } else {
                throw new PagingDataInvalidException("amount: " + amount + " page: " + page);
            }
            return new ApiResponse<>(HttpStatus.OK.value(),INFO_RETRIEVED_SUCCESSFULLY,pagedAndFilteredOnes.get());
        } else {
            throw new OrderTypeInvalidException("order type NONE is not valid");
        }
    }

    @GetMapping(value = "/qp/{amount}/{page}/{orderby}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ApiResponse<ArrayNode> getPagedShowsByOrderBy(@PathVariable int amount,
                                                            @PathVariable int page,
                                                            @PathVariable OrderBy orderby,
                                                            @RequestParam("filter") List<String> filters) {
        if(orderby != OrderBy.NONE) {
            ArrayNode filteredOnes = showService.applyFilters(filters, orderby);
            Optional<ArrayNode> pagedAndFilteredOnes;
            if(amount > 0 && page >= 0) {
                pagedAndFilteredOnes = Optional.ofNullable(showService.applyPaging(filteredOnes, amount, page));
                if(!pagedAndFilteredOnes.isPresent()) {
                    throw new PageIndexNotFoundException("page required is out of scope");
                }
            } else {
                throw new PagingDataInvalidException("amount: " + amount + " page: " + page);
            }
            return new ApiResponse<>(HttpStatus.OK.value(),INFO_RETRIEVED_SUCCESSFULLY,pagedAndFilteredOnes.get());
        } else {
            throw new OrderTypeInvalidException("order type NONE is not valid");
        }
    }

    @GetMapping(value = "/qp/{amount}/{page}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ApiResponse<ArrayNode> getPagedShowsWithDefaults(@PathVariable int amount,
                                                               @PathVariable int page,
                                                               @RequestParam("filter") List<String> filters) {
        ArrayNode filteredOnes = showService.applyFilters(filters);
        Optional<ArrayNode> pagedAndFilteredOnes;
        if(amount > 0 && page >= 0) {
            pagedAndFilteredOnes = Optional.ofNullable(showService.applyPaging(filteredOnes, amount, page));
            if(!pagedAndFilteredOnes.isPresent()) {
                throw new PageIndexNotFoundException("page required is out of scope");
            }
        } else {
            throw new PagingDataInvalidException("amount: " + amount + " page: " + page);
        }
        return new ApiResponse<>(HttpStatus.OK.value(),INFO_RETRIEVED_SUCCESSFULLY,pagedAndFilteredOnes.get());
    }

    @GetMapping(value = "/q/{orderby}/{orientation}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ApiResponse<ArrayNode> getShowsByOrderByAndOrientation(@PathVariable OrderBy orderby,
                                                                     @PathVariable Orientation orientation,
                                                                     @RequestParam("filter") List<String> filters) {
        if(orderby != OrderBy.NONE) {
            ArrayNode filteredOnes = showService.applyFilters(filters, orderby, orientation);
            return new ApiResponse<>(HttpStatus.OK.value(),INFO_RETRIEVED_SUCCESSFULLY,filteredOnes);
        } else {
            throw new OrderTypeInvalidException("order type NONE is not valid");
        }
    }

    @GetMapping(value = "/q/{orderby}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ApiResponse<ArrayNode> getShowsByOrderBy(@PathVariable OrderBy orderby,
                                                       @RequestParam("filter") List<String> filters) {
        if(orderby != OrderBy.NONE) {
            ArrayNode filteredOnes = showService.applyFilters(filters, orderby);
            return new ApiResponse<>(HttpStatus.OK.value(),INFO_RETRIEVED_SUCCESSFULLY,filteredOnes);
        } else {
            throw new OrderTypeInvalidException("order type NONE is not valid");
        }
    }

    @GetMapping(value = "/q", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ApiResponse<ArrayNode> getShowsWithDefaults(@RequestParam("filter") List<String> filters) {
        ArrayNode filteredOnes = showService.applyFilters(filters);
        return new ApiResponse<>(HttpStatus.OK.value(),INFO_RETRIEVED_SUCCESSFULLY,filteredOnes);
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ApiResponse<String> getShowById(@PathVariable int id) {
        RestTemplate restTemplate = new RestTemplate();
        String tvmazeResourceUrl = urlShowById.replace("{id}",Integer.toString(id));
        return new ApiResponse<>(HttpStatus.OK.value(),INFO_RETRIEVED_SUCCESSFULLY,
                restTemplate.getForEntity(tvmazeResourceUrl, String.class).getBody());
    }

    @GetMapping(value = "/{id}/{date}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ApiResponse<String> getEpisodeByDate(@PathVariable int id, @PathVariable String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date, ISO_LOCAL_DATE);
        } catch(Exception ex) {
            throw new DateFormatInvalidException("date format is invalid " + ex.getMessage());
        }
        RestTemplate restTemplate = new RestTemplate();
        String tvmazeResourceUrl = urlEpisodeByDate
                .replace("{id}",Integer.toString(id))
                .concat(localDate.toString());
        return new ApiResponse<>(HttpStatus.OK.value(),INFO_RETRIEVED_SUCCESSFULLY,
                restTemplate.getForEntity(tvmazeResourceUrl, String.class).getBody());
    }
}
