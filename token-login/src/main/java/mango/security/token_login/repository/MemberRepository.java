package mango.security.token_login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mango.security.token_login.entity.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.Optional;

/**
 * JpaRepository는 인터페이스로서, CRUD 기능을 제공한다.
 * JpaRepository의 제네릭 타입은 엔티티 타입과 식별자 타입이다.
 * JpaRepository는 ListPagingAndSortingRepository, ListCrudRepository, QueryByExampleExecutor를 직접 상속 받는다.
 * {@link ListPagingAndSortingRepository} {@link ListCrudRepository} {@link QueryByExampleExecutor}
 * 또한 ListCrudRepository는 CrudRepository를 상속받고, ListPagingAndSortingRepository는 PagingAndSortingRepository를 상속받는다.
 * {@link CrudRepository} {@link PagingAndSortingRepository}
 * 마지막으로 CrudRepository와 PagingAndSortingRepository는 Repository를 상속받는다.
 * {@link Repository}
 */

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmail(String email);
}

/**
 * JpaRepository를 보면 @NoRepositoryBean이라는 어노테이션이 붙어있다.
 * @see NoRepositoryBean
 * 이 어노테이션은 스프링 데이터 JPA가 이 인터페이스를 빈으로 등록하지 않도록 하는 역할을 한다.
 * 즉, 인터페이스 자체가 직접 빈으로등록되는 것이 아닌, JpaRepository를 상속받는 인터페이스가 빈으로 등록된다.
 * 여기서는 MemberRepository가 빈으로 등록된다.
 */