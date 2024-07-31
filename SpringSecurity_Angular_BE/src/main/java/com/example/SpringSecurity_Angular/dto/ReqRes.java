package com.example.SpringSecurity_Angular.dto;

import com.example.SpringSecurity_Angular.entity.OurUsers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
/*
@JsonInclude(JsonInclude.Include.NON_NULL)

Mục đích: Annotation này hướng dẫn Jackson loại trừ các trường có giá trị null khỏi kết quả JSON.
        Tác động: Khi chuyển đổi một đối tượng Java sang JSON, bất kỳ trường nào có giá trị null sẽ bị bỏ qua khỏi biểu diễn JSON. Việc này có thể giúp giảm kích thước của đầu ra JSON và khiến nó trở nên súc tích hơn.
@JsonIgnoreProperties(ignoreUnknown = true)

Mục đích: Annotation này cho Jackson bỏ qua bất kỳ thuộc tính nào trong dữ liệu JSON đầu vào không có trường tương ứng trong đối tượng Java.
        Tác động: Trong quá trình giải mã (chuyển đổi JSON sang đối tượng Java), nếu dữ liệu JSON chứa các thuộc tính không được định nghĩa là trường trong lớp Java, Jackson sẽ âm thầm bỏ qua các thuộc tính đó và không gây ra lỗi nào. Điều này có thể hữu ích khi xử lý dữ liệu JSON có thể có các thuộc tính bổ sung, tùy chọn mà bạn không cần thiết phải xử lý trong mã Java của mình.
*/
public class ReqRes {
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String city;
    private String role;
    private String email;
    private String password;
    private OurUsers ourUsers;
    private List<OurUsers> ourUsersList;
}
