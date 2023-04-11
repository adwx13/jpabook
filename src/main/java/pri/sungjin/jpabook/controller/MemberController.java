package pri.sungjin.jpabook.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pri.sungjin.jpabook.domain.Address;
import pri.sungjin.jpabook.domain.Member;
import pri.sungjin.jpabook.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("members/new")
    public String create(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";

    }

    @GetMapping("/members")
    public String list(Model model) {
        //현재 모델로 member entity를 넘기고 있는데 이는 별로좋지않다
        //-> entity를 그대로 화면에서 쓰다보면 화면관련된 코드를 추가하게 된다
        //-> 추후 entity가 더러워지는데 이런현상이 일어나지 않기위해 dto를 별도로 만들어 넘겨주는게 좋다

        //웹 화면에서 현재 entity를 임의로 노출하고 있지만 api를 만드는 경우 entity를 절대로 반환하면 안된다
        //entity가 유지보수하며 지속적으로 변경되는데 entity를 수정시 기존만들어진 api의 스펙이 변경되기 때문이다.
        model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }
}
