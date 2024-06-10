package com.teamsprta.todoList.domain.duty.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.teamsparta.todoList.TodoListApplication
import com.teamsparta.todoList.domain.duty.dto.duty.AddDutyRequestDto
import com.teamsparta.todoList.domain.duty.dto.duty.DutyCommentsResponseDto
import com.teamsparta.todoList.domain.duty.dto.duty.DutyResponseDto
import com.teamsparta.todoList.domain.duty.dto.duty.UpdateDutyRequestDto
import com.teamsparta.todoList.domain.duty.service.DutyService
import com.teamsparta.todoList.infra.security.jwt.JwtPlugin
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import java.time.LocalDate


@SpringBootTest(classes = [TodoListApplication::class])
@AutoConfigureMockMvc //mockMvc 주입
@ExtendWith(MockKExtension::class) //mockk쓸때 써주기
@ComponentScan(basePackages = ["com.teamsparta.todoList"])
class DutyControllerTest @Autowired constructor(
    private val mockMvc: MockMvc, //컨트롤러 테스트일경우!
    private val jwtPlugin: JwtPlugin,

    @MockBean
    private val dutyService: DutyService,

    ) : DescribeSpec({

    extension(SpringExtension)

    afterContainer {
        clearAllMocks()
    }


    describe("GET/dutyList") {
        context("로그인 한 회원이 조회요청을 보낼 때")
        {
            it("200 status code를 응답해야한다.") {

                val mockDutyList = mutableListOf(
                    DutyCommentsResponseDto(
                        id = 1L,
                        title = "Test Title 1",
                        description = "Test Description 1",
                        date = LocalDate.now(),
                        name = "Test User 1",
                        complete = false,
                        comments = mutableListOf()
                    ),
                    DutyCommentsResponseDto(
                        id = 2L,
                        title = "Test Title 2",
                        description = "Test Description 2",
                        date = LocalDate.now(),
                        name = "Test User 2",
                        complete = true,
                        comments = mutableListOf()

                    )
                )

                //Mock동작 정의
                every { dutyService.getAllDutyList("desc") } returns mockDutyList

                // AccessToken 생성
                val jwtToken = jwtPlugin.generateAccessToken(
                    subject = "1",
                    email = "test@gmail.com",
                    role = "member"
                )


                /**perform : http요청 실행 -> 결과 반환*/
                // 요청 수행
                val result = mockMvc.perform(
                    get("/duties")
                        .header("Authorization", "Bearer $jwtToken")
                        //요청해더에 authorization 헤더 추가해서 Jwt토큰 포함
                        .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()

                //status code 확인
                result.response.status shouldBe 200

                // json string으로부터 DutyCommentsResponseDto 리스트 생성
                val responseList = jacksonObjectMapper().readValue(
                    result.response.contentAsString,
                    Array<DutyCommentsResponseDto>::class.java
                ).toList()

                // 리스트 크기 확인
                responseList.size shouldBe mockDutyList.size

                // 각 DutyCommentsResponseDto 객체의 필드 값 확인
                responseList[0].id shouldBe mockDutyList[0].id
                responseList[0].title shouldBe mockDutyList[0].title
                responseList[1].id shouldBe mockDutyList[1].id
                responseList[1].title shouldBe mockDutyList[1].title

            }
        }
    }

    describe("GET /duties/name") {
        context("로그인 한 회원이 작성자로 조회요청을 보낼 때") {
            it("200 status code를 응답해야한다.") {
                val mockDutyList = listOf(
                    DutyResponseDto(
                        id = 1L,
                        title = "Test Title 1",
                        description = "Test Description 1",
                        date = LocalDate.now(),
                        name = "Test User 1",
                        complete = false,
                        userId = 1L
                    ),
                    DutyResponseDto(
                        id = 2L,
                        title = "Test Title 2",
                        description = "Test Description 2",
                        date = LocalDate.now(),
                        name = "Test User 2",
                        complete = true,
                        userId = 2L
                    )
                )

                every { dutyService.getDutyListByName(any()) } returns mockDutyList

                val jwtToken = jwtPlugin.generateAccessToken(subject = "1", email = "test@gmail.com", role = "member")

                val result = mockMvc.perform(
                    get("/duties/name").param("filterName", "Test User")
                        .header("Authorization", "Bearer $jwtToken")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()

                result.response.status shouldBe 200

                val responseList = jacksonObjectMapper().readValue(
                    result.response.contentAsString,
                    Array<DutyResponseDto>::class.java
                ).toList()

                responseList.size shouldBe mockDutyList.size
                responseList[0].id shouldBe mockDutyList[0].id
                responseList[0].title shouldBe mockDutyList[0].title
            }
        }
    }

    describe("GET /duties/{dutyId}") {
        context("로그인 한 사람이 단건조회요청을 보낼 때") {
            it("200 status code를 응답해야한다.") {
                val dutyId = 1L
                val mockDuty = DutyCommentsResponseDto(
                    id = dutyId,
                    title = "Test Title",
                    description = "Test Description",
                    date = LocalDate.now(),
                    name = "Test User",
                    complete = false,
                    comments = mutableListOf()
                )

                every { dutyService.getDutyById(any()) } returns mockDuty

                val jwtToken = jwtPlugin.generateAccessToken(subject = "1", email = "test@gmail.com", role = "member")

                val result = mockMvc.perform(
                    get("/duties/$dutyId")
                        .header("Authorization", "Bearer $jwtToken")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()

                result.response.status shouldBe 200

                val responseDto = jacksonObjectMapper().readValue(
                    result.response.contentAsString,
                    DutyCommentsResponseDto::class.java
                )

                responseDto.id shouldBe dutyId
            }
        }
    }

    describe("POST /duties") {
        context("로그인 한 사람이 추가요청을 보낼 때") {
            it("201 created status code를 응답해야한다.") {
                val addDutyRequest = AddDutyRequestDto(
                    title = "New Duty",
                    description = "New Description",
                    date = LocalDate.now(),
                    name = "Test User"
                )
                val mockDutyResponse = DutyResponseDto(
                    id = 1L,
                    title = addDutyRequest.title,
                    description = addDutyRequest.description,
                    date = addDutyRequest.date,
                    name = "Test User",
                    complete = false,
                    userId = 1L
                )

                every { dutyService.addDuty(any()) } returns mockDutyResponse

                val jwtToken = jwtPlugin.generateAccessToken(subject = "1", email = "test@gmail.com", role = "member")

                val result = mockMvc.perform(
                    post("/duties")
                        .header("Authorization", "Bearer $jwtToken")
                        .content(jacksonObjectMapper().writeValueAsString(addDutyRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                    //Request객체 -> json문자열로 변환 -> 요청 본문으로 전송
                ).andReturn()

                result.response.status shouldBe 201

                val responseDto = jacksonObjectMapper().readValue(
                    result.response.contentAsString,
                    DutyResponseDto::class.java
                )

                responseDto.id shouldBe mockDutyResponse.id
            }
        }

        context("bindingResult가 에러를 보내면") {
            it("runtime exception을 응답해야 한다.") {
                val invalidAddDutyRequest =
                    AddDutyRequestDto(title = "", description = "", date = LocalDate.now(), name = "Test User")

                val jwtToken = jwtPlugin.generateAccessToken(subject = "1", email = "test@gmail.com", role = "member")

                val result = mockMvc.perform(
                    post("/duties")
                        .header("Authorization", "Bearer $jwtToken")
                        .content(jacksonObjectMapper().writeValueAsString(invalidAddDutyRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()

                result.response.status shouldBe 400
            }
        }
    }


    describe("PUT /duties/{dutyId}") {
        context("해당글을 작성 한 사람이 수정요청을 보낼 때") {
            it("201 created status code를 응답해야한다.") {
                val dutyId = 1L
                val updateDutyRequest = UpdateDutyRequestDto(
                    title = "Updated Title",
                    description = "Updated Description",
                    name = "Test User"
                )
                val mockDutyResponse = DutyResponseDto(
                    id = dutyId,
                    title = updateDutyRequest.title,
                    description = updateDutyRequest.description,
                    date = LocalDate.now(),
                    name = "Test User",
                    complete = false,
                    userId = 1L
                )

                every { dutyService.updateDuty(any(), any()) } returns mockDutyResponse

                val jwtToken = jwtPlugin.generateAccessToken(subject = "1", email = "test@gmail.com", role = "member")

                val result = mockMvc.perform(
                    put("/duties/$dutyId")
                        .header("Authorization", "Bearer $jwtToken")
                        .content(jacksonObjectMapper().writeValueAsString(updateDutyRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()

                result.response.status shouldBe 200

                val responseDto = jacksonObjectMapper().readValue(
                    result.response.contentAsString,
                    DutyResponseDto::class.java
                )

                responseDto.id shouldBe dutyId
            }
        }


    }

    describe("DELETE /duties/{dutyId}") {
        context("해당글을 작성 한 사람이 수정을 보낼 때") {
            it("204 no content status code를 응답해야한다.") {
                val dutyId = 1L

                every { dutyService.deleteDuty(any()) } returns Unit

                val jwtToken = jwtPlugin.generateAccessToken(subject = "1", email = "test@gmail.com", role = "member")

                val result = mockMvc.perform(
                    delete("/duties/$dutyId")
                        .header("Authorization", "Bearer $jwtToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andReturn()

                result.response.status shouldBe 204
            }
        }


    }

})


