
package com.junsoo.project.carinventorymanagement.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DeleteRequest {
    List<Integer> ids;
}
