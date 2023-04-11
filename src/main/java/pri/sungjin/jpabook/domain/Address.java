package pri.sungjin.jpabook.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class Address {

    private String city;
    private String street;

    private String zipcode;

    //JPA사용시 기본생성자가 필요하기때문에 만들어둔것
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
