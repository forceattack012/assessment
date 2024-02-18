package com.kbtg.bootcamp.posttest.users.model;

import java.util.List;

public record ReportLotteriesDTO(List<String> tickets, int count, int cost) {}
