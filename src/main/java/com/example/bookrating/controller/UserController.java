package com.example.bookrating.controller;

import com.example.bookrating.dto.LoginDTO;
import com.example.bookrating.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class UserController {

    @PostMapping("/loginInfo")
    public ResponseEntity<LoginDTO> login(){
        LoginDTO loginDTO = new LoginDTO();
        UserDTO userDTO = new UserDTO();

        userDTO.setId(1L);
        userDTO.setEmail("user@example.com");
        userDTO.setUserName("user123");
        userDTO.setAvatar("https://lh3.googleusercontent.com/fife/ALs6j_FOrMrn68TfDlHfS7QdLNo2d8LYH-dbQtuoNwukOkMNhOXcM3jyL141UrSN71mtcqpd4dc59EUw5S3XTRT_sv1r7yVfC_xAsa8wlB4SnJT8f8g9Yd0zPLEtlxIyfKz7p2omJgX6bdP029hbzxf9T5JIjmun8w5DLyLOm0EZ1u0KnP-P2gov8vZCaeZjUaJSADrr3mfVM2lyr2pt6Ak7JPIUCv8OCquMHtLH_-zg1bpWFI8aXzQWGhwqX9UcxH93DVSXDemI4mkf68CVIOU9v9Al6kLBbVpN9vsZLLa-kul1ZJCRmB4OBch4QEIiPcBk8DR4MnPEXA2bya2EFC5yKRb8I_ApCfmUG1fKsCaBjKGWPILY0vE6KP7YrjalIChBcyKx8O-I0k2LiBQi-T_3l6ioqJzwu6Qe-KE5Vhb9fCtb47BD0wKXvgPdFPMlxkhvmqnfYzi27Ye8bEqiDnJ8AHwDW49l0-78CoZr-nUjIEmgBLZ1sEVOxj1VzRXwaZ9-D3b2CmDwK8zizrMOZ2LXtGt2_zubjXzt46qvoisIary4aHjpXZ4SEpGz2CpUEz4wW_bbUX0EbKCoIxLFG9plG9yrbtB6lzgfDP4bGOM-up5Dq2mEAiflrLboptTFQnn0i9J9n-kOx1MefO174FlqsECm0g268hkT7uDPYLJLzzfhnCfwY3XQUWEZnChh18Tf_XVRezDL_2qSol_LK2Yw1kafaGZG3wxKe63G-emQAqxvMlHbDiW_LMRljugWvBbQZqb0QZssTNr6u1CRojrCIdu4LDHZDjHrLyMETwF9Sm2GC6zz9SscGnG-bWsioW6qYIzSZ3J2XyGy81QhFOdX5FwzvaoLlJSfk74eiamKxwZFqtq-I4vySUT7R-sY56EiaX7qO2EKgTpVz2sQl0geSQxf9DTZEmeq2zScgh851N5FV340SFIu-YulxdSsphbX8hU4j7_mUoDpGJMEi7_afBjX7CsJ_pReoToIi9NrdebJA77CBoaw-qgZSyl2AV4WpR84fnluaFR3jsGLNMXAL_xp_d2Nt_jFqm1kJeRMiCmBOWGzLJzc6-hFug715tQAbGdO3fHA4mjsNkZekciGnUSuRTqrpNh3yZC2UVZ0in0wekhS-GQETb2VNgakN64smmFpYfpUYI0FUEe58JY2lumsqtypUCtGPXYaYmEXif-MU6LEF4-Jqt_TaKrCGtj_R-iAtLD0lfqOLxTAV_3I2_JF_1ZaPaY04tsF6xK81b60sSdD6MGtYd6q2BVnf1a_L8iQExVCcddTxd5kheIc0rck7F1SC4ShpofYrymoB3NKiLAXxi36e5NrQ1Ngih4elsUXPIgn2gKBCe92z_83_tAZ4ibyO7oiNXcUb2pgAXsa-MhCdqPkEZ6xQZMIPw=s64-c");

        loginDTO.setAccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");
        loginDTO.setUser(userDTO);

        return ResponseEntity.ok(loginDTO);
    }
}
