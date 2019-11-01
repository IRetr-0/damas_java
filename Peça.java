package br.mackenzie.fci.ec.lp2;

/**
 * Classe que representa uma peça do jogo.
 *
 *
 */
public class Peça
{
	private final CorDePeça cor;

	/**
	 * Construtor de Peça, que recebe uma cor.
	 *
	 * @param cor um valor do enum CorDePeça
	 */
	public Peça(CorDePeça cor)
	{
		this.cor = cor;
	}

	/**
	 * Método get para o atributo cor. Embora ele seja final...
	 *
	 * @return a cor desta instância de Peça
	 */
	public CorDePeça getCor()
	{
		return this.cor;
	}
}
