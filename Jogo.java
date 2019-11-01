package br.mackenzie.fci.ec.lp2;

import java.util.Scanner;

/**
 * Define uma partida de um jogo de damas.
 *
 * 
 */
public class Jogo
{
	private final Scanner scanner = new Scanner(System.in);
	private final Peça[][] tabuleiro = new Peça[8][8];
	private boolean vezDoSegundoJogador = false;

	/**
	 * Constrói um jogo preenchendo um tabuleiro 8x8 com peças brancas e pretas.
	 */
	public Jogo()
	{
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < tabuleiro[i].length; j += 2)
				tabuleiro[i][j + i % 2] = new Peça(CorDePeça.BRANCA);
		}

		for (int i = tabuleiro.length - 1; i > tabuleiro.length - 4; --i) {
			for (int j = 0; j < tabuleiro[i].length; j += 2)
				tabuleiro[i][j + i % 2] = new Peça(CorDePeça.PRETA);
		}
	}

	/**
	 * Imprime o tabuleiro do jogo atual na forma de uma matriz de caracteres na
	 * tela, em que uma peça branca é representada por b, uma peça preta, por p,
	 * e um espaço sem peça, por ' '. Por ter tratado as posições iniciais da
	 * matriz tabuleiro como coordenadas iniciais, as linhas são iteradas na
	 * ordem inversa. No caso de damas, B marca uma dama branca. P, uma dama
	 * preta.
	 */
	public void imprimirTabuleiro()
	{
		for (int i = this.tabuleiro.length - 1; i > -1; --i) {
			System.out.print((i + 1) + "  ");

			for (Peça peça : this.tabuleiro[i]) {
				if (peça == null)
					System.out.print("  ");
				else if (peça.getCor() == CorDePeça.BRANCA)
					System.out.print((peça instanceof Dama) ? "B " : "b ");
				else // if (peça.getCor() == CorDePeça.PRETA) <- é um enum
					System.out.print((peça instanceof Dama) ? "P " : "p ");
			}

			System.out.println();
		}

		System.out.println("   A B C D E F G H");
	}

	/**
	 * Dá um "tick" no jogo: a mesma coisa que uma "vez", um turno.
	 */
	public void step()
	{
		String posição;
		int linhaOrigem, colunaOrigem;
		int linhaDestino, colunaDestino;

		System.out.println("Vez do jogador "
				+ ((vezDoSegundoJogador) ? "2" : "1"));

		System.out.print("Digite a posição da peça que você deseja mover(e.g. "
				+ "B4): ");

		posição = this.scanner.next();
		colunaOrigem = posição.charAt(0) - 'A';
		linhaOrigem = posição.charAt(1) - '1';

		System.out.print("Digite a posição aonde você deseja mover a peça: ");

		posição = this.scanner.next();
		colunaDestino = posição.charAt(0) - 'A';
		linhaDestino = posição.charAt(1) - '1';

		if (moverPeça(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino)) {
			System.out.println("Peça movida com sucesso!");
			vezDoSegundoJogador = !vezDoSegundoJogador;
		} else {
			System.out.println("Movimento inválido!");
		}

		System.out.println();
		imprimirTabuleiro();
	}

	/**
	 * Retorna o status do jogo: 0 para um jogo que ainda está em sessão, 2 para
	 * um jogo em que o segundo jogador venceu, 1 para um jogo em que o primeiro
	 * jogador venceu.
	 *
	 * @return inteiro no intervalo fechado [0, 2]
	 */
	public int status()
	{
		int pretas, brancas;

		pretas = contarPeças(CorDePeça.PRETA);
		brancas = contarPeças(CorDePeça.BRANCA);

		if (pretas > 0 && brancas > 0)
			return 0;
		else if (brancas > 0)
			return 1;
		else
			return 2;
	}

	/**
	 * Método privado para a funcionalidade de mover uma peça.
	 *
	 * @param  linhaOrigem   linha da peça a ser movida
	 * @param  colunaorigem  coluna da peça a ser movida
	 * @param  linhaDestino  linha para onde a peça vai
	 * @param  colunaDestino coluna para onde a peça vai
	 * @return               caso a peça tenha sido movida com sucesso
	 */
	private boolean moverPeça(int linhaOrigem, int colunaOrigem,
			int linhaDestino, int colunaDestino)
	{
		Peça peça, novoEspaço;

		// lida com casos absurdos logo de início
		if (linhaDestino == linhaOrigem || colunaDestino == colunaOrigem ||
				Math.abs(linhaDestino - linhaOrigem) > 1   ||
				Math.abs(colunaDestino - colunaOrigem) > 1 ||
				linhaDestino < 0  || linhaOrigem < 0  || // índices negativos
				colunaDestino < 0 || colunaOrigem < 0 ||
				linhaDestino > 7  || linhaOrigem > 7  || // índices altos
				colunaDestino > 7 || colunaOrigem > 7)
			return false;

		peça = this.tabuleiro[linhaOrigem][colunaOrigem];
		novoEspaço = this.tabuleiro[linhaDestino][colunaDestino];

		if (peça == null) // não tem peça na posição de origem
			return false;

		if (peça instanceof Dama) // damas podem ir para frente ou para trás
			;
		else if (peça.getCor() == CorDePeça.BRANCA &&
				linhaDestino < linhaOrigem) // peças brancas, só para cima
			return false;
		else if (peça.getCor() == CorDePeça.PRETA &&
				linhaDestino > linhaOrigem) // peças pretas, para baixo
			return false;

		if ((peça.getCor() == CorDePeça.BRANCA && vezDoSegundoJogador) ||
				(peça.getCor() == CorDePeça.PRETA && !vezDoSegundoJogador))
			return false;

		if (novoEspaço != null) // existindo peça no destino, tente comer
			return comerPeça(linhaOrigem, colunaOrigem,
					linhaDestino, colunaDestino);

		this.tabuleiro[linhaDestino][colunaDestino] = peça;
		this.tabuleiro[linhaOrigem][colunaOrigem] = null;

		if ((linhaDestino == 7 && !vezDoSegundoJogador) ||
				(linhaDestino == 0 && vezDoSegundoJogador))
			this.tabuleiro[linhaDestino][colunaDestino] =
				new Dama(peça.getCor());

		return true;
	}

	/**
	 * Método privado que conta o número de peças de certa cor atualmente no
	 * tabuleiro.
	 *
	 * @param  cor CorDePeça procurada
	 * @return     o número de peças da cor recebida
	 */
	private int contarPeças(CorDePeça cor)
	{
		int númeroDePeças = 0;

		for (Peça[] linha : this.tabuleiro) {
			for (Peça peça : linha) {
				if (peça != null && peça.getCor() == cor)
					++númeroDePeças;
			}
		}

		return númeroDePeças;
	}

	/**
	 * Método privado para a funcionalidade de comer uma peça. Só deve ser
	 * chamado a partir de moverPeça(). Não há garantia de que funcione fora
	 * deste.
	 *
	 * @param  linhaOrigem   linha da peça a ser movida
	 * @param  colunaorigem  coluna da peça a ser movida
	 * @param  linhaDestino  linha da peça a ser comida
	 * @param  colunaDestino coluna da peça a ser comida
	 * @return               caso a peça tenha sido comida com sucesso
	 */
	private boolean comerPeça(int linhaOrigem, int colunaOrigem,
			int linhaDestino, int colunaDestino)
	{
		Peça caçador, presa, novoEspaço;

		int novaLinha = 2*linhaDestino - linhaOrigem;
		int novaColuna = 2*colunaDestino - colunaOrigem;

		if (novaLinha < 0 || novaLinha > 7 || novaColuna < 0 || novaColuna > 7)
			return false;

		caçador = this.tabuleiro[linhaOrigem][colunaOrigem];
		presa = this.tabuleiro[linhaDestino][colunaDestino];
		novoEspaço = this.tabuleiro[novaLinha][novaColuna];

		if (novoEspaço != null || caçador.getCor() == presa.getCor())
			return false;

		this.tabuleiro[linhaDestino][colunaDestino] = null;
		this.tabuleiro[novaLinha][novaColuna] = caçador;
		this.tabuleiro[linhaOrigem][colunaOrigem] = null;

		if ((novaLinha == 7 && !vezDoSegundoJogador) ||
				(novaLinha == 0 && vezDoSegundoJogador))
			this.tabuleiro[novaLinha][novaColuna] =
				new Dama(caçador.getCor());

		return true;
	}
}
