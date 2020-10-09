package br.com.alura.forum.config.validacao;

/*
 * representa a classe de erro onde irá mostrar qual campo e a mensgam de erro
 */
public class ErroDeFormDto {
	private String campo;
	private String erro;

	/**
	 * @param campo
	 * @param erro
	 */
	public ErroDeFormDto(String campo, String erro) {
		this.campo = campo;
		this.erro = erro;
	}

	public String getCampo() {
		return campo;
	}

	public String getErro() {
		return erro;
	}

}
