package com.search.medicinesearch.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.search.medicinesearch.model.Medicine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public List<Medicine> autocomplete(String text) throws IOException {
        Query prefixQuery = QueryBuilders.prefix(p -> p
                .field("name")
                .value(text)
                .boost(2.0f)  
        );

        Query fuzzyQuery = QueryBuilders.match(m -> m
                .field("name")
                .query(text)
                .fuzziness("AUTO")
        );

        Query combinedQuery = QueryBuilders.bool(b -> b
                .should(prefixQuery)
                .should(fuzzyQuery)
                .minimumShouldMatch("1")
        );

        SearchRequest request = SearchRequest.of(s -> s
                .index("medicines")
                .query(combinedQuery)
                .minScore(0.6d)
                .size(10)
        );

        SearchResponse<Medicine> response = elasticsearchClient.search(request, Medicine.class);

        return response.hits().hits().stream()
                .map(hit -> hit.source())
                .collect(Collectors.toList());
    }
}
