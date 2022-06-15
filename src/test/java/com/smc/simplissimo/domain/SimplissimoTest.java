package com.smc.simplissimo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.smc.simplissimo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SimplissimoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Simplissimo.class);
        Simplissimo simplissimo1 = new Simplissimo();
        simplissimo1.setId(1L);
        Simplissimo simplissimo2 = new Simplissimo();
        simplissimo2.setId(simplissimo1.getId());
        assertThat(simplissimo1).isEqualTo(simplissimo2);
        simplissimo2.setId(2L);
        assertThat(simplissimo1).isNotEqualTo(simplissimo2);
        simplissimo1.setId(null);
        assertThat(simplissimo1).isNotEqualTo(simplissimo2);
    }
}
