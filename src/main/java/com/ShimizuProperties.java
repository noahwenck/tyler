package com;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Component for Shimizu API related properties
 */
@Component
public class ShimizuProperties {

    @Value("${shimizu.url}")
    private String shimizuUrl;

    public String getShimizuUrl() {
        return shimizuUrl;
    }
}
