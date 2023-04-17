package pri.sungjin.jpabook;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpabookApplication {

	public static void main(String[] args) {
		Hello hello = new Hello();
		hello.setData("hello");
		String data = hello.getData();
		System.out.println("hello == "+data);

		SpringApplication.run(JpabookApplication.class, args);
	}

	/**
	 * Proxy객체를 임의 객체로 변경하도록 만드는 내용
	 * 일대일, 일대다, 다대일 과 같은 경우 별도 조인을 통해 데이터를 가져오는데 이때 JPA Proxy객체를 임의로 넣어둔다
	 * 따라서 해당 소스가 없이 호출시 응답값을 읽을 수 없는 Proxy객체라 에러를 발생시킨다.
	 * with 이것과 같이 다른한쪽에는 JsonIgnore를 써주어야 한다.
	 * -> 그냥 entity를 외부로 노출해주지 않을 것이기 때문에 해당 소스도 없어지고 엔티티를 반환하는 로직도 없어져야 한다.
	 */
	@Bean
	Hibernate5JakartaModule hibernate5JakartaModule() {
		return new Hibernate5JakartaModule();
	}

}
