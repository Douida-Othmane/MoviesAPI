package oth.dev.movies;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ContextConfiguration(classes = {ReviewController.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    @Autowired
    private ReviewController reviewController;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private final String PREFIX_PATH = "/api/v1/reviews";

    /**
     * Method under test: {@link ReviewController#createReview(Map)}
     */
    @Test
    void testCreateReview() throws Exception {
        Review review = new Review();
        ObjectId id = new ObjectId();
        review.setId(id);
        review.setBody("This is a review");

        Map<String, String> payload = new HashMap<>();
        payload.put("body", "This is a review");
        payload.put("imdbId", "tt10298840");

        Mockito.when(reviewService.createReview(anyString(), anyString())).thenReturn(review);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post(PREFIX_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(payload));

        mockMvc.perform(mockRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.body", CoreMatchers.is(review.getBody())));
    }
}

