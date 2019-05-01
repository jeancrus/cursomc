package com.jean.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.jean.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		Calendar cal = Calendar.getInstance(); //instanciando calendar
		cal.setTime(instanteDoPedido); //inserindo a data do instante
		cal.add(Calendar.DAY_OF_MONTH, 7); //adicionando 7 dias no instante
		pagto.setDataVencimento(cal.getTime());
		
		/*
		 * Numa situação real, teria que chamar uma webservice que gera o boleto 
		 */
	}
}
