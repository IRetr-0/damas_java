package br.mackenzie.fci.ec.lp2;

import java.util.concurrent.TimeUnit;

/**
 * Classe principal do programa, será responsável por administrar um Jogo.
 *
 * 
 */
public class JogoDeDamas
{
	public static void main(String[] args)
	{
		Jogo jogo = new Jogo();

		jogo.imprimirTabuleiro();

		game(jogo);
	}

	public static void game(Jogo jogo)
	{
		while (jogo.status() == 0)
			jogo.step();

		System.out.println("Jogador " + jogo.status() + " venceu!");
	}
}
