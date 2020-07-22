# Spring Security & JWT

> https://jeong-pro.tistory.com/205



![jwt_auth_filter](C:\github\ssafy\summary\img\jwt_auth_filter.png)



1. 클라이언트로부터의 요청이 오면 AppicationFilter(정의한 필터들) 객체들로 가게 된다.

2. 필터들을 거치다가 DelegatingFilterProxyRegistrationBean 필터를 만남

3. 이 필터는 DelegatingFilterProxy라는 클래스로 만들어진 스프링 Bean을 등록시켜주는 역할

   > 이 클래스는 Spring security가 모든 애플리케이션 요청을 감싸게 해서 모든 요청에 보안이 적용되게 하는 서블릿 필터 (Spring Framework에서 제공)
   
4. DelegatingFilterProxy 클래스인 **springSecurityFilterChain**이라는 이름의 스프링 빈을 등록하고 이후에는 이 DelegatingFilterProxy(springSecurityFilterChain)가 필터로 동작

   >
   >구체적으로, DelegatingFilterProxy가 처리를 위임하는 필터 클래스는 FilterChainProxy다. 이 클래스 내부에 체인으로 등록된 필터를 순서대로 수행
   >
   >(DelegatingFilterProxy → FilterChainProxy → List 구조)
   >
   >> → 앞에서 스프링 시큐리티가 만든 DelegatingFilterProxy라고 했는데 스프링 부트를 기준으로 설명해서 그렇고 그냥 스프링에서는 직접 web.xml파일에 필터를 등록을 한다.
   
5. springSecurityFilterChain은 스프링에서 보안과 관련된 여러 필터 리스트를 갖고 있는 객체로 필터 리스트를 순회하면서 필터링을 실시