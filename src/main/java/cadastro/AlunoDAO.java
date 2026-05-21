package cadastro;

import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    private List<Aluno> alunos = new ArrayList<>();
    private int contador = 1;

    public void adicionarAluno(String nome, int idade) {
        Aluno aluno = new Aluno(contador++, nome, idade);
        alunos.add(aluno);
    }

    public List<Aluno> listarAlunos() {
        return alunos;
    }

    public void atualizarAluno(int id, String nome, int idade) {
        for (Aluno aluno : alunos) {
            if (aluno.getId() == id) {
                aluno.setNome(nome);
                aluno.setIdade(idade);
                break;
            }
        }
    }

    public void removerAluno(int id) {
        alunos.removeIf(aluno -> aluno.getId() == id);
    }
}