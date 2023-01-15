package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    // 클라이언트인 OrderServiceImpl 이 DiscountPolicy 인터페이스 뿐만 아니라
    // FixDiscountPolicy 인 구체 클래스도 함께 의존하고 있다. 실제 코드를 보면 의존하고 있다! DIP 위반

//    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
    // 지금 코드는 기능을 확장해서 변경하면, 클라이언트 코드에 영향을 준다! 따라서 OCP를 위반한다.

    private DiscountPolicy discountPolicy;
    // 인터페이스에만 의존하도록 설계와 코드를 변경했다.
    // 현재 상테에서 실행을 해보면 NPE(null pointer exception)가 발생한다.
    // 이 문제를 해결하려면 누군가가 클라이언트인 OrderServiceImpl에
    // DiscountPolicy 의 구현 객체를 대신 생성하고 주입해주어야 한다.

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
