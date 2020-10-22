package br.unip.tcc.eiaapp.util;

import java.util.List;

import br.unip.tcc.eiaapp.DTO.HumorDTO;
import br.unip.tcc.eiaapp.DTO.MensagemWatsonDTO;
import br.unip.tcc.eiaapp.DTO.UserDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CallAPI {
    @POST("watson")
    Call<MensagemWatsonDTO> enviaMensagem(@Body MensagemWatsonDTO mensagemWatsonDTO);

    @POST("humor")
    Call<HumorDTO> gravaHumor(@Body HumorDTO humorDTO);

    @POST("humores")
    Call<List<HumorDTO>> obtemHumores(@Body UserDTO user);
}
