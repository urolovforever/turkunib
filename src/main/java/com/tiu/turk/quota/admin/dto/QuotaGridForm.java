package com.tiu.turk.quota.admin.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuotaGridForm {
    private Long scholarshipId;
    private List<QuotaRowForm> rows = new ArrayList<>();
}
