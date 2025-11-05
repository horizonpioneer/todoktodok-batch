package todoktodok.batch.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import todoktodok.batch.entity.Book;
import todoktodok.batch.entity.Comment;
import todoktodok.batch.entity.Discussion;
import todoktodok.batch.entity.Member;
import todoktodok.batch.entity.Reply;
import todoktodok.batch.repository.BookRepository;
import todoktodok.batch.repository.CommentRepository;
import todoktodok.batch.repository.DiscussionRepository;
import todoktodok.batch.repository.MemberRepository;
import todoktodok.batch.repository.ReplyRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataGenerationJobConfig {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final DiscussionRepository discussionRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    private final Random random = new Random();

    @Bean
    public Job dataGenerationJob(JobRepository jobRepository, Step memberGenerationStep,
                                  Step bookGenerationStep, Step discussionGenerationStep,
                                  Step commentGenerationStep, Step replyGenerationStep) {
        return new JobBuilder("dataGenerationJob", jobRepository)
                .start(memberGenerationStep)
                .next(bookGenerationStep)
                .next(discussionGenerationStep)
                .next(commentGenerationStep)
                .next(replyGenerationStep)
                .build();
    }

    @Bean
    public Step memberGenerationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("memberGenerationStep", jobRepository)
                .tasklet(memberGenerationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step bookGenerationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("bookGenerationStep", jobRepository)
                .tasklet(bookGenerationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step discussionGenerationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("discussionGenerationStep", jobRepository)
                .tasklet(discussionGenerationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step commentGenerationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("commentGenerationStep", jobRepository)
                .tasklet(commentGenerationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step replyGenerationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("replyGenerationStep", jobRepository)
                .tasklet(replyGenerationTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet memberGenerationTasklet() {
        return (contribution, chunkContext) -> {
            int count = 10000; // 생성할 회원 수
            List<Member> members = new ArrayList<>();

            log.info("회원 데이터 생성 시작: {}명", count);

            String[] roles = {"백엔드 개발자", "프론트엔드 개발자", "풀스택 개발자", "데이터 엔지니어", "DevOps 엔지니어", "안드로이드 개발자", "iOS 개발자"};

            for (int i = 1; i <= count; i++) {
                String role = roles[random.nextInt(roles.length)];

                Member member = Member.builder()
                        .email("user" + i + "@test.com")
                        .nickname("개발자" + i)
                        .profileImage("https://example.com/profile/" + i + ".jpg")
                        .profileMessage(role + "입니다. 기술 서적을 통해 성장하고 있습니다!")
                        .build();
                members.add(member);

                if (i % 100 == 0) {
                    memberRepository.saveAll(members);
                    members.clear();
                    log.info("회원 {}명 저장 완료", i);
                }
            }

            if (!members.isEmpty()) {
                memberRepository.saveAll(members);
            }

            log.info("회원 데이터 생성 완료: 총 {}명", count);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet bookGenerationTasklet() {
        return (contribution, chunkContext) -> {
            int count = 20000; // 생성할 책 수
            List<Book> books = new ArrayList<>();

            log.info("책 데이터 생성 시작: {}권", count);

            String[] genres = {"프로그래밍", "웹 개발", "데이터 과학", "인공지능", "알고리즘", "네트워크", "데이터베이스", "클라우드", "운영체제", "컴퓨터 구조", "소프트웨어 공학", "보안"};
            String[] publishers = {"한빛미디어", "위키북스", "에이콘", "인사이트", "제이펍", "길벗"};

            for (int i = 1; i <= count; i++) {
                String genre = genres[random.nextInt(genres.length)];
                String publisher = publishers[random.nextInt(publishers.length)];

                Book book = Book.builder()
                        .title(genre + " 완벽 가이드 " + i)
                        .summary("이것은 " + genre + " 분야의 기술 서적입니다. 입문자부터 전문가까지 실무에 필요한 핵심 개념과 활용법을 다룹니다. "
                                + "다양한 예제 코드와 함께 체계적으로 학습할 수 있으며, 실전 프로젝트에 바로 적용 가능한 실용적인 내용을 담고 있습니다.")
                        .author("저자" + i)
                        .publisher(publisher)
                        .isbn(String.format("97%011d", i))
                        .image("https://example.com/book/" + i + ".jpg")
                        .build();
                books.add(book);

                if (i % 100 == 0) {
                    bookRepository.saveAll(books);
                    books.clear();
                    log.info("책 {}권 저장 완료", i);
                }
            }

            if (!books.isEmpty()) {
                bookRepository.saveAll(books);
            }

            log.info("책 데이터 생성 완료: 총 {}권", count);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet discussionGenerationTasklet() {
        return (contribution, chunkContext) -> {
            int count = 1000000; // 생성할 토론 수
            List<Member> members = memberRepository.findAll();
            List<Book> books = bookRepository.findAll();

            if (members.isEmpty() || books.isEmpty()) {
                log.error("회원 또는 책 데이터가 없습니다.");
                return RepeatStatus.FINISHED;
            }

            List<Discussion> discussions = new ArrayList<>();

            log.info("토론 데이터 생성 시작: {}개", count);

            String[] topics = {"코드 구현", "설계 패턴", "알고리즘", "성능 최적화", "아키텍처", "실전 활용", "예제 분석", "개념 정리", "실습 과제"};

            for (int i = 1; i <= count; i++) {
                Member randomMember = members.get(random.nextInt(members.size()));
                Book randomBook = books.get(random.nextInt(books.size()));
                String topic = topics[random.nextInt(topics.length)];

                Discussion discussion = Discussion.builder()
                        .title(randomBook.getTitle() + "의 " + topic + "에 대한 토론 " + i)
                        .content("이 책의 " + topic + "에 대해 이야기해봅시다. 여러분의 의견을 자유롭게 남겨주세요. "
                                + "실무에 적용하면서 느낀 점이나 더 좋은 방법이 있다면 공유 부탁드립니다. "
                                + "저는 이 부분이 특히 유용했는데요, 다른 분들은 어떻게 활용하고 계신지 궁금합니다.")
                        .viewCount((long) random.nextInt(1000))
                        .member(randomMember)
                        .book(randomBook)
                        .build();
                discussions.add(discussion);

                if (i % 500 == 0) {
                    discussionRepository.saveAll(discussions);
                    discussions.clear();
                    log.info("토론 {}개 저장 완료", i);
                }
            }

            if (!discussions.isEmpty()) {
                discussionRepository.saveAll(discussions);
            }

            log.info("토론 데이터 생성 완료: 총 {}개", count);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet commentGenerationTasklet() {
        return (contribution, chunkContext) -> {
            int count = 200000; // 생성할 댓글 수
            List<Member> members = memberRepository.findAll();
            List<Discussion> discussions = discussionRepository.findAll();

            if (members.isEmpty() || discussions.isEmpty()) {
                log.error("회원 또는 토론 데이터가 없습니다.");
                return RepeatStatus.FINISHED;
            }

            List<Comment> comments = new ArrayList<>();

            log.info("댓글 데이터 생성 시작: {}개", count);

            String[] reactions = {
                    "정말 공감됩니다!",
                    "좋은 의견이네요.",
                    "저도 비슷하게 생각했어요.",
                    "다른 관점에서 생각해볼 수 있겠네요.",
                    "실용적인 접근이네요.",
                    "이 부분은 저는 다르게 구현했는데요.",
                    "상세한 설명 감사합니다.",
                    "실무에 바로 적용해봤습니다.",
                    "코드 예제가 도움이 되었어요."
            };

            for (int i = 1; i <= count; i++) {
                Member randomMember = members.get(random.nextInt(members.size()));
                Discussion randomDiscussion = discussions.get(random.nextInt(discussions.size()));
                String reaction = reactions[random.nextInt(reactions.length)];

                Comment comment = Comment.builder()
                        .content(reaction + " 제 프로젝트에서도 비슷한 케이스가 있었는데 이 방법을 적용해보니 효과적이었습니다. (댓글 " + i + ")")
                        .member(randomMember)
                        .discussion(randomDiscussion)
                        .build();
                comments.add(comment);

                if (i % 1000 == 0) {
                    commentRepository.saveAll(comments);
                    comments.clear();
                    log.info("댓글 {}개 저장 완료", i);
                }
            }

            if (!comments.isEmpty()) {
                commentRepository.saveAll(comments);
            }

            log.info("댓글 데이터 생성 완료: 총 {}개", count);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet replyGenerationTasklet() {
        return (contribution, chunkContext) -> {
            int count = 100000; // 생성할 답글 수
            List<Member> members = memberRepository.findAll();
            List<Comment> comments = commentRepository.findAll();

            if (members.isEmpty() || comments.isEmpty()) {
                log.error("회원 또는 댓글 데이터가 없습니다.");
                return RepeatStatus.FINISHED;
            }

            List<Reply> replies = new ArrayList<>();

            log.info("답글 데이터 생성 시작: {}개", count);

            String[] reactions = {
                    "맞아요!",
                    "저도 그렇게 생각해요.",
                    "좋은 지적입니다.",
                    "감사합니다!",
                    "동의합니다.",
                    "새로운 시각이네요."
            };

            for (int i = 1; i <= count; i++) {
                Member randomMember = members.get(random.nextInt(members.size()));
                Comment randomComment = comments.get(random.nextInt(comments.size()));
                String reaction = reactions[random.nextInt(reactions.length)];

                Reply reply = Reply.builder()
                        .content(reaction + " 답글 " + i)
                        .member(randomMember)
                        .comment(randomComment)
                        .build();
                replies.add(reply);

                if (i % 1000 == 0) {
                    replyRepository.saveAll(replies);
                    replies.clear();
                    log.info("답글 {}개 저장 완료", i);
                }
            }

            if (!replies.isEmpty()) {
                replyRepository.saveAll(replies);
            }

            log.info("답글 데이터 생성 완료: 총 {}개", count);
            return RepeatStatus.FINISHED;
        };
    }
}
