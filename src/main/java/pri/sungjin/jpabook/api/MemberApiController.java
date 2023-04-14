package pri.sungjin.jpabook.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pri.sungjin.jpabook.domain.Member;
import pri.sungjin.jpabook.service.MemberService;

import java.util.List;
import java.util.stream.Collectors;

//Controller와 ResponseBody를 합친 RestController를 사용
//@Controller @ResponseBody
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * Entity를 그대로 노출하게 되면 api에서 보여지길 원하지 않는 필드또한 보여지게 된다
     * 물론 entity에 JsonIgnore를 추가하여 해당필드를 보여주지 않도록 할 수 있지만 이는 entity에 presentation영역의 코드가 추가되는 꼴이다
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();

        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);

    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }


    /**
     * API의 요청값은 entity를 요구하고 있는데 이는 좋지않다
     * entity의 변경이 이루어졌을 때 api스펙또한 변경되기 때문이다 따라서 api스펙을 위한 dto를 별도로 만들어야 한다.
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * Entity가 아닌 Dto를 쓰는 경우 필요한 API스펙만 받아올 수 있다.
     * Entity를 그대로 쓰는 경우 받고싶지 않은 필드 또한 같이 받을 수 있기 때문에 dto를 써야한다.
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.name);

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    //수정의 경우 put을 사용 (멱등성)
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        //update후 member를 바로 반환해줄 수 있지만 update 관련 메소드와 select관련 메소드를 별도로 관리되어진다고 생각하여 비즈니스 로직을 짠다.
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());

    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class UpdateMemberRequest {
        private String name;

    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;

    }


}
