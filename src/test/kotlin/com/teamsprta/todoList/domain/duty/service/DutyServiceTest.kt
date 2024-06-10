package com.teamsprta.todoList.domain.duty.service

import com.teamsparta.todoList.domain.duty.comment.repository.CommentRepository
import com.teamsparta.todoList.domain.duty.dto.AddDutyRequestDto
import com.teamsparta.todoList.domain.duty.dto.CompleteDutyRequestDto
import com.teamsparta.todoList.domain.duty.dto.UpdateDutyRequestDto
import com.teamsparta.todoList.domain.duty.model.Duty
import com.teamsparta.todoList.domain.duty.repository.DutyRepository
import com.teamsparta.todoList.domain.duty.service.DutyServiceImpl
import com.teamsparta.todoList.domain.exception.ModelNotFoundException
import com.teamsparta.todoList.infra.security.UserPrincipal
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import java.time.LocalDate


@SpringBootTest
@AutoConfigureMockMvc //mockMvc 주입
@ExtendWith(MockKExtension::class)
class DutyServiceTest :BehaviorSpec( {
    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }

    // 종속 repository mocking
    val dutyRepository = mockk<DutyRepository>()
    val commentRepository = mockk<CommentRepository>()

    val dutyService = DutyServiceImpl(dutyRepository, commentRepository)

    Given("Duty 리스트가 존재할 때") {
        When("전체 Duty 리스트를 요청하면") {
            Then("전체 Duty 리스트가 반환되어야 한다") {
               //given
                val dutyList = listOf(
                    Duty( title = "Test Title 1", description = "Test Description 1", date = LocalDate.now(), name = "Test User 1", userId = 1L),
                    Duty(title = "Test Title 2", description = "Test Description 2", date = LocalDate.now(), name = "Test User 2", userId = 2L)
                )
                every { dutyRepository.findAll() } returns dutyList

                //when
                val result = dutyService.getAllDutyList()

                //then
                result.size shouldBe 2
            }
        }
    }

    Given("Duty 리스트가 존재하지 않을 때") {
        When("전체 Duty 리스트를 요청하면") {
            Then("빈 리스트가 반환되어야 한다") {
                //given
                every { dutyRepository.findAll() } returns emptyList()

                //when
                val result = dutyService.getAllDutyList()

                //then
                result.size shouldBe 0
            }
        }
    }

    Given("특정 Duty가 존재할 때") {
        When("특정 Duty를 요청하면") {
            Then("해당 Duty가 반환되어야 한다") {
                //given
                val dutyId = 1L
                val duty = Duty( title = "Test Title", description = "Test Description", date = LocalDate.now(), name = "Test User", userId = 1L).apply { id = dutyId }

                every { dutyRepository.findByIdOrNull(dutyId) } returns duty

                //when
                val result = dutyService.getDutyById(dutyId)

                //then
                result.id shouldBe dutyId
            }
        }
    }

    Given("특정 Duty가 존재하지 않을 때") {
        When("특정 Duty를 요청하면") {
            Then("ModelNotFoundException이 발생해야 한다") {
                //given
                val dutyId = 1L

                every { dutyRepository.findByIdOrNull(dutyId) } returns null

                //when
                //then
                shouldThrow<ModelNotFoundException> {
                    //예외를 던질 것으로 예상되는 코드 (when)
                    dutyService.getDutyById(dutyId)
                }
            }
        }
    }

    Given("특정 작성자 이름으로 Duty가 존재할 때") {
        When("작성자 이름으로 조회를 요청하면") {
            Then("해당 작성자의 Duty 리스트가 반환되어야 한다") {
                //given
                val filterName = "Test User"
                val dutyList = listOf(
                    Duty(title = "Test Title 1", description = "Test Description 1", date = LocalDate.now(), name = filterName, userId = 1L)
                )

                every { dutyRepository.findByName(filterName) } returns dutyList

                //when
                val result = dutyService.getDutyListByName(filterName)

                //then
               for(i in result){
                   i.name shouldBe filterName
               }
            }
        }
    }

    Given("특정 작성자 이름으로 Duty가 존재하지 않을 때") {
        When("작성자 이름으로 조회를 요청하면") {
            Then("빈 리스트가 반환되어야 한다") {
                //given
                val filterName = "Unknown User"

                every { dutyRepository.findByName(filterName) } returns emptyList()

                //when
                val result = dutyService.getDutyListByName(filterName)

                //then
                result.size shouldBe 0
            }
        }
    }

    Given("사용자가 할 일을 추가할 때") {
        When("할 일을 추가하면") {
            Then("추가된 Duty가 반환되어야 한다") {
                //given
                val addDutyRequest = AddDutyRequestDto(title = "New Duty", description = "New Description", date = LocalDate.now(), name = "Test User")
                val currentUser = UserPrincipal(1L, "test@gmail.com",setOf("member")) //인증된 사용자 객체
                // val currentUser = UserPrincipal(1L, "test@gmail.com", setOf("USER", "ADMIN")) // 여러 권한 설정가능

                every { dutyRepository.save(any()) } returns Duty(
                    title = addDutyRequest.title,
                    description = addDutyRequest.description,
                    date = addDutyRequest.date,
                    name = addDutyRequest.name,
                    userId = currentUser.id
                ).apply { id = 1L }

                val securityContext = SecurityContextImpl() //Security context 수동설정
                securityContext.authentication = UsernamePasswordAuthenticationToken(currentUser, null, listOf()) //인증정보 설정
                //securityContext.authentication = UsernamePasswordAuthenticationToken(currentUser, null, currentUser.authorities)

                SecurityContextHolder.setContext(securityContext)

                //when
                val result = dutyService.addDuty(addDutyRequest)

                //then
                result.title shouldBe addDutyRequest.title
                result.description shouldBe addDutyRequest.description
            }
        }
    }

    Given("게시글 작성자가 할 일을 수정할 때") {
        When("작성자가 할 일을 수정하면") {
            Then("수정된 Duty가 반환되어야 한다") {
                //given
                val dutyId = 1L
                val currentUser = UserPrincipal(1L, "test@gmail.com",setOf("member"))
                val duty = Duty(title = "Old Title", description = "Old Description", date = LocalDate.now(), name = "Test User", userId = currentUser.id)
                val updateDutyRequest = UpdateDutyRequestDto(title = "Updated Title", description = "Updated Description", name = "Test User")

                every { dutyRepository.findByIdOrNull(dutyId) } returns duty
                every { dutyRepository.save(any()) } returns duty

                val securityContext = SecurityContextImpl()
                securityContext.authentication = UsernamePasswordAuthenticationToken(currentUser, null, listOf())
                SecurityContextHolder.setContext(securityContext)

                //when
                val result = dutyService.updateDuty(dutyId, updateDutyRequest)

                //then
                result.title shouldBe updateDutyRequest.title
                result.description shouldBe updateDutyRequest.description
            }
        }
    }

    Given("게시글 작성자가 아닌 사람이 할 일을 수정할 때") {
        When("작성자가 아닌 사람이 할 일을 수정하면") {
            Then("IllegalAccessException이 발생해야 한다") {
                //given
                val dutyId = 1L
                val currentUser = UserPrincipal(2L, "test2@gmail.com",setOf("member"))
                val duty = Duty(title = "Old Title", description = "Old Description", date = LocalDate.now(), name = "Test User", userId = 1L)
                val updateDutyRequest = UpdateDutyRequestDto(title = "Updated Title", description = "Updated Description", name = "Test User")

                every { dutyRepository.findByIdOrNull(dutyId) } returns duty

                val securityContext = SecurityContextImpl()
                securityContext.authentication = UsernamePasswordAuthenticationToken(currentUser, null, listOf())
                SecurityContextHolder.setContext(securityContext)

                //when,then
                shouldThrow<IllegalAccessException> {
                    dutyService.updateDuty(dutyId, updateDutyRequest)
                }
            }
        }
    }

    Given("게시글 작성자가 할 일을 삭제할 때") {
        When("작성자가 할 일을 삭제하면") {
            Then("해당 Duty가 삭제되어야 한다") {
                val dutyId = 1L
                val currentUser = UserPrincipal(1L, "test@gmail.com",setOf("member"))
                val duty = Duty( title = "Old Title", description = "Old Description", date = LocalDate.now(), name = "Test User", userId = currentUser.id)

                every { dutyRepository.findByIdOrNull(dutyId) } returns duty
                every { dutyRepository.delete(duty) } just Runs

                val securityContext = SecurityContextImpl()
                securityContext.authentication = UsernamePasswordAuthenticationToken(currentUser, null, listOf())
                SecurityContextHolder.setContext(securityContext)

                //when
                dutyService.deleteDuty(dutyId)

                //then
                verify { dutyRepository.delete(duty) } //메서드가 호출되었는지 확인
            }
        }
    }

    Given("게시글 작성자가 아닌 사람이 할 일을 삭제할 때") {
        When("작성자가 아닌 사람이 할 일을 삭제하면") {
            Then("IllegalAccessException이 발생해야 한다") {
                //given
                val dutyId = 1L
                val currentUser = UserPrincipal(2L, "test2@gmail.com",setOf("member"))
                val duty = Duty( title = "Old Title", description = "Old Description", date = LocalDate.now(), name = "Test User", userId = 1L)

                every { dutyRepository.findByIdOrNull(dutyId) } returns duty

                val securityContext = SecurityContextImpl()
                securityContext.authentication = UsernamePasswordAuthenticationToken(currentUser, null, listOf())
                SecurityContextHolder.setContext(securityContext)


                //when,then
                shouldThrow<IllegalAccessException> {
                    dutyService.deleteDuty(dutyId)
                }
            }
        }
    }

    Given("게시글 작성자가 할 일 완료 여부를 수정할 때") {
        When("작성자가 할 일 완료 여부를 수정하면") {
            Then("완료 여부가 수정된 Duty가 반환되어야 한다") {
                //given
                val dutyId = 1L
                val currentUser = UserPrincipal(1L, "test@gmail.com",setOf("member"))
                val duty = Duty(title = "Old Title", description = "Old Description", date = LocalDate.now(), name = "Test User", userId = currentUser.id, complete = false)
                val completeDutyRequest = CompleteDutyRequestDto(complete = true)

                every { dutyRepository.findByIdOrNull(dutyId) } returns duty
                every { dutyRepository.save(any()) } returns duty

                val securityContext = SecurityContextImpl()
                securityContext.authentication = UsernamePasswordAuthenticationToken(currentUser, null, listOf())
                SecurityContextHolder.setContext(securityContext)

                //when
                val result = dutyService.completeDuty(dutyId, completeDutyRequest)

                //then
                result.complete shouldBe true
            }
        }
    }

    Given("게시글 작성자가 아닌 사람이 할 일 완료 여부를 수정할 때") {
        When("작성자가 아닌 사람이 할 일 완료 여부를 수정하면") {
            Then("IllegalAccessException이 발생해야 한다") {
                //given
                val dutyId = 1L
                val currentUser = UserPrincipal(2L, "test2@gmail.com",setOf("member"))
                val duty = Duty( title = "Old Title", description = "Old Description", date = LocalDate.now(), name = "Test User", userId = 1L, complete = false)
                val completeDutyRequest = CompleteDutyRequestDto(complete = true)

                every { dutyRepository.findByIdOrNull(dutyId) } returns duty

                val securityContext = SecurityContextImpl()
                securityContext.authentication = UsernamePasswordAuthenticationToken(currentUser, null, listOf())
                SecurityContextHolder.setContext(securityContext)

                //when,then
                shouldThrow<IllegalAccessException> {
                    dutyService.completeDuty(dutyId, completeDutyRequest)
                }
            }
        }
    }
})









