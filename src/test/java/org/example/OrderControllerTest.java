package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ItemRequestDTO;
import org.example.dto.OrderRequestDTO;
import org.example.entity.Item;
import org.example.entity.User;
import org.example.repository.ItemRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private Item savedItem;
    private User savedUser;

    @BeforeEach
    public void setUp() {
        itemRepository.deleteAll();
        userRepository.deleteAll();

        savedItem = itemRepository.save(new Item("Monitor"));
        savedUser = userRepository.save(new User("Charlie Brown", "charlie@example.com"));
    }

    @Test
    public void testCreateItemEndpoint() throws Exception {
        ItemRequestDTO requestDTO = new ItemRequestDTO();
        requestDTO.setName("Monitor 4K");

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Monitor 4K"));
    }

    @Test
    public void testCreateOrderEndpoint() throws Exception {
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setItemId(1L);
        requestDTO.setQuantity(2);
        requestDTO.setUserId(1L);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.quantityRequested").value(2));
    }

    @Test
    public void testListAllOrdersEndpoint() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}