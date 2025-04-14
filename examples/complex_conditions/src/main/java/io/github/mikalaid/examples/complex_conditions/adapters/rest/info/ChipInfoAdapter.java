package io.github.mikalaid.examples.complex_conditions.adapters.rest.info;

import io.github.mikalaid.examples.complex_conditions.application.port.out.info.ChipInfoPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;

@Service
@SessionScope
public class ChipInfoAdapter implements ChipInfoPort {

    private final String chipId;
    public ChipInfoAdapter(@Value("#{request.getHeader('ChipId')}") final String chipId) {
        this.chipId = chipId;
    }

    @Override
    public BigDecimal getAnimalAge() {
        return mockHttpRequestAndAnswer();
    }

    private BigDecimal mockHttpRequestAndAnswer(){
        for(char c : this.chipId.toCharArray()){
            if(Character.isDigit(c)){
                return new BigDecimal(String.valueOf(c));
            }
        }
        throw new RuntimeException();
    }
}
