package com.realpage.tvmaze.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.realpage.tvmaze.domain.consts.OrderBy;
import com.realpage.tvmaze.domain.consts.Orientation;
import com.realpage.tvmaze.exceptions.PartitionGenreJsonProcessingException;
import com.realpage.tvmaze.repositories.memrepo.ShowRepositoryInMemory;
import com.realpage.tvmaze.utils.Compare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// https://www.geeksforgeeks.org/iterative-quick-sort/

@Service
public class ShowService implements IShowService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ShowRepositoryInMemory showRepositoryInMemory;

    @Override
    public ArrayNode applyFilters(List<String> filters) {
        return applyFilters(filters, OrderBy.NONE);
    }

    @Override
    public ArrayNode applyFilters(List<String> filters, OrderBy ob) {
        return applyFilters(filters, ob, Orientation.ASC);
    }

    @Override
    public ArrayNode applyFilters(List<String> filters, OrderBy ob, Orientation o) {

        String[] keywords = new String[0];
        String[] language = new String[0];
        String[] genres = new String[0];
        String[] channel = new String[0];
        String[] days = new String[0];
        String[] time = new String[0];

        for (String filter : filters) {
            if (filter.startsWith("keywords:")) {
                filter = filter.replace("keywords:", "");
                keywords = filter.split("-");
            } else if(filter.startsWith("language:")) {
                filter = filter.replace("language:", "");
                language = filter.split("-");
            } else if(filter.startsWith("genres:")) {
                filter = filter.replace("genres:", "");
                genres = filter.split("-");
            } else if(filter.startsWith("channel:")) {
                filter = filter.replace("channel:", "");
                channel = filter.split("-");
            } else if(filter.startsWith("days:")) {
                filter = filter.replace("days:", "");
                days = filter.split("-");
            } else if(filter.startsWith("time:")) {
                filter = filter.replace("time:", "");
                time = filter.split("-");
            }
        }

        ArrayNode arrayNode = showRepositoryInMemory.findAll();
        ArrayNode arrayNodeFiltered = filtering(keywords,arrayNode,"name");
        arrayNodeFiltered = filtering(language,arrayNodeFiltered,"language");
        arrayNodeFiltered = filtering(genres,arrayNodeFiltered,"genres");
        arrayNodeFiltered = filtering(channel,arrayNodeFiltered,"network","name");
        arrayNodeFiltered = filtering(days,arrayNodeFiltered,"schedule","days");
        arrayNodeFiltered = filtering(time,arrayNodeFiltered,"schedule","time");

        if(arrayNodeFiltered.size() > 1 && ob != OrderBy.NONE) {
            try {
                quickSortIterative(arrayNodeFiltered, 0, arrayNodeFiltered.size() - 1, ob, o);
            } catch (JsonProcessingException e) {
                throw new PartitionGenreJsonProcessingException("JsonProcessingException");
            }
        }

        return arrayNodeFiltered;
    }

    private ArrayNode filtering(String[] filter, ArrayNode arrayNode, final String field) {
        if(filter.length == 0) return arrayNode;
        ArrayNode filtered = mapper.createArrayNode();
        arrayNode.forEach(jsonNode -> {
            int count = 0;
            for(String key : filter) {
                String klc = key.toLowerCase();
                if(jsonNode.get(field) != null) {
                    if (jsonNode.get(field).isArray()) {
                        for (JsonNode objNode : jsonNode.get(field)) {
                            if (objNode.asText().toLowerCase().contains(klc)) {
                                count++;
                                break;
                            }
                        }
                    } else {
                        if (jsonNode.get(field).asText().toLowerCase().contains(klc)) {
                            count++;
                        }
                    }
                }
            }
            if(count == filter.length) {
                filtered.add(jsonNode);
            }
        });
        return filtered;
    }

    private ArrayNode filtering(String[] filter, ArrayNode arrayNode, final String field, final String prop) {
        if(filter.length == 0) return arrayNode;
        ArrayNode filtered = mapper.createArrayNode();
        arrayNode.forEach(jsonNode -> {
            int count = 0;
            for(String key : filter) {
                String klc = key.toLowerCase();
                if(jsonNode.get(field) != null && jsonNode.get(field).get(prop) != null) {
                    if (jsonNode.get(field).get(prop).isArray()) {
                        for (JsonNode objNode : jsonNode.get(field).get(prop)) {
                            if (objNode.asText().toLowerCase().contains(klc)) {
                                count++;
                                break;
                            }
                        }
                    } else {
                        if (jsonNode.get(field).get(prop).asText().toLowerCase().contains(klc)) {
                            count++;
                        }
                    }
                }
            }
            if(count == filter.length) {
                filtered.add(jsonNode);
            }
        });
        return filtered;
    }

    @Override
    public ArrayNode getAllShows() {
        return showRepositoryInMemory.findAll();
    }

    /*
        This function takes last element as pivot,
        places the pivot element at its correct
        position in sorted array, and places all
        smaller (smaller than pivot) to left of
        pivot and all greater elements to right
        of pivot
    */
    private int partitionRating(ArrayNode arr, int low, int high, Orientation o) {

        double pivot = 0;
        if(arr.get(high).get("rating")!=null && arr.get(high).get("rating").get("average")!=null) {
            pivot = arr.get(high).get("rating").get("average").asDouble();
        }

        // index of smaller element
        int i = (low - 1);
        for (int j = low; j <= high - 1; j++) {

            double val = 0;
            if(arr.get(j).get("rating")!=null && arr.get(j).get("rating").get("average")!=null) {
                val = arr.get(j).get("rating").get("average").asDouble();
            }

            // If current element is smaller than or
            // equal to pivot
            if (Compare.Doubles(val, pivot, o)) {
                i++;

                // swap arr[i] and arr[j]
                JsonNode temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        JsonNode temp = arr.get(i + 1);
        arr.set(i + 1, arr.get(high));
        arr.set(high, temp);

        return i + 1;
    }

    private int partitionChannel(ArrayNode arr, int low, int high, Orientation o) {

        String pivot = "";
        if(arr.get(high).get("network")!=null && arr.get(high).get("network").get("name")!=null) {
            pivot = arr.get(high).get("network").get("name").asText();
        }

        // index of smaller element
        int i = (low - 1);
        for (int j = low; j <= high - 1; j++) {

            String val = "";
            if(arr.get(j).get("network")!=null && arr.get(j).get("network").get("name")!=null) {
                val = arr.get(j).get("network").get("name").asText();
            }

            // If current element is smaller than or
            // equal to pivot
            if (Compare.Strings(val, pivot, o)) {
                i++;

                // swap arr[i] and arr[j]
                JsonNode temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        JsonNode temp = arr.get(i + 1);
        arr.set(i + 1, arr.get(high));
        arr.set(high, temp);

        return i + 1;
    }

    private int partitionGenre(ArrayNode arr, int low, int high, Orientation o) throws JsonProcessingException {

        JsonNode pivot = Optional.ofNullable(arr
                .get(high)
                .get("genres"))
                .orElse(mapper.readTree("[]"));

        // index of smaller element
        int i = (low - 1);
        for (int j = low; j <= high - 1; j++) {

            JsonNode val = Optional.ofNullable(arr
                    .get(j)
                    .get("genres"))
                    .orElse(mapper.readTree("[]"));

            // If current element is smaller than or
            // equal to pivot
            if (Compare.JNodeArrays(val, pivot, o)) {
                i++;

                // swap arr[i] and arr[j]
                JsonNode temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        JsonNode temp = arr.get(i + 1);
        arr.set(i + 1, arr.get(high));
        arr.set(high, temp);

        return i + 1;
    }

    /*
        A[] --> Array to be sorted,
        l  --> Starting index,
        h  --> Ending index
    */
    private void quickSortIterative(ArrayNode arr, int l, int h, OrderBy ob, Orientation o) throws JsonProcessingException {

        // Create an auxiliary stack
        int[] stack = new int[h - l + 1];

        // initialize top of stack
        int top = -1;

        // push initial values of l and h to stack
        stack[++top] = l;
        stack[++top] = h;

        // Keep popping from stack while is not empty
        while (top >= 0) {
            // Pop h and l
            h = stack[top--];
            l = stack[top--];

            // Set pivot element at its correct position
            // in sorted array
            int p;
            if(ob == OrderBy.CHANNEL) {
                p = partitionChannel(arr, l, h, o);
            } else if(ob == OrderBy.GENRE) {
                p = partitionGenre(arr, l, h, o);
            } else {
                p = partitionRating(arr, l, h, o);
            }

            // If there are elements on left side of pivot,
            // then push left side to stack
            if (p - 1 > l) {
                stack[++top] = l;
                stack[++top] = p - 1;
            }

            // If there are elements on right side of pivot,
            // then push right side to stack
            if (p + 1 < h) {
                stack[++top] = p + 1;
                stack[++top] = h;
            }
        }
    }

    @Override
    public ArrayNode applyPaging(ArrayNode arrayNode, int amount, int page) {
        int index = page * amount; // page start in 0
        ArrayNode array = mapper.createArrayNode();

        if(index==0 && arrayNode.size()==0) return array;

        if(index < arrayNode.size()) {
            int top = Math.min(index + amount, arrayNode.size());
            for (int i = index; i < top; i++) {
                array.add(arrayNode.get(i));
            }
            return array;
        } else {
            return null;
        }
    }
}
