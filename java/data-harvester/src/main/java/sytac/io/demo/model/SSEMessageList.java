package sytac.io.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SSEMessageList {
    private List<SSEMessage> serverMessages;
}
