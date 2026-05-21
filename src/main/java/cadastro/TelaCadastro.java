package cadastro;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class TelaCadastro extends JFrame {

    private AlunoDAO dao = new AlunoDAO();

    private JTextField txtNome;
    private JFormattedTextField txtIdade;
    private JTextField txtId;
    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabela;

    public TelaCadastro() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Cadastro de Alunos");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Formulário
        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBorder(BorderFactory.createTitledBorder("Cadastro de Aluno"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;

        // ID
        c.gridx = 0; c.gridy = 0;
        painelForm.add(new JLabel("ID:"), c);
        txtId = new JTextField();
        txtId.setEditable(false);
        c.gridx = 1; c.gridy = 0;
        painelForm.add(txtId, c);

        // Nome
        c.gridx = 0; c.gridy = 1;
        painelForm.add(new JLabel("Nome:"), c);
        txtNome = new JTextField();
        c.gridx = 1; c.gridy = 1;
        painelForm.add(txtNome, c);

        // Idade (somente números)
        c.gridx = 0; c.gridy = 2;
        painelForm.add(new JLabel("Idade:"), c);
        NumberFormat format = NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(150);
        formatter.setAllowsInvalid(false);
        txtIdade = new JFormattedTextField(formatter);
        c.gridx = 1; c.gridy = 2;
        painelForm.add(txtIdade, c);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.setBackground(new Color(76, 175, 80));
        btnAdicionar.setForeground(Color.WHITE);
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBackground(new Color(33, 150, 243));
        btnAtualizar.setForeground(Color.WHITE);
        JButton btnRemover = new JButton("Remover");
        btnRemover.setBackground(new Color(244, 67, 54));
        btnRemover.setForeground(Color.WHITE);

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);

        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        painelForm.add(painelBotoes, c);

        add(painelForm, BorderLayout.NORTH);

        // Tabela estilizada
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Idade"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 2) ? Integer.class : String.class;
            }
        };
        tabelaAlunos = new JTable(modeloTabela);
        tabelaAlunos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaAlunos.setRowHeight(25);
        tabelaAlunos.setShowGrid(true);
        tabelaAlunos.setGridColor(Color.LIGHT_GRAY);

        JScrollPane scroll = new JScrollPane(tabelaAlunos);
        scroll.setBorder(BorderFactory.createTitledBorder("Lista de Alunos"));
        add(scroll, BorderLayout.CENTER);

        // Eventos
        btnAdicionar.addActionListener(e -> adicionarAluno());
        btnAtualizar.addActionListener(e -> atualizarAluno());
        btnRemover.addActionListener(e -> removerAluno());

        tabelaAlunos.getSelectionModel().addListSelectionListener(e -> preencherCampos());

        listarAlunos();
        setVisible(true);
    }

    private void adicionarAluno() {
        String nome = txtNome.getText().trim();
        if (nome.isEmpty() || txtIdade.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idade = ((Number)txtIdade.getValue()).intValue();
        dao.adicionarAluno(nome, idade);
        limparCampos();
        listarAlunos();
        JOptionPane.showMessageDialog(this, "Aluno adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void atualizarAluno() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        String nome = txtNome.getText().trim();
        int idade = ((Number)txtIdade.getValue()).intValue();
        dao.atualizarAluno(id, nome, idade);
        limparCampos();
        listarAlunos();
        JOptionPane.showMessageDialog(this, "Aluno atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void removerAluno() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        dao.removerAluno(id);
        limparCampos();
        listarAlunos();
        JOptionPane.showMessageDialog(this, "Aluno removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void preencherCampos() {
        int linha = tabelaAlunos.getSelectedRow();
        if (linha != -1) {
            txtId.setText(modeloTabela.getValueAt(linha,0).toString());
            txtNome.setText(modeloTabela.getValueAt(linha,1).toString());
            txtIdade.setValue(modeloTabela.getValueAt(linha,2));
        }
    }

    private void listarAlunos() {
        modeloTabela.setRowCount(0);
        for (Aluno a : dao.listarAlunos()) {
            modeloTabela.addRow(new Object[]{a.getId(), a.getNome(), a.getIdade()});
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtIdade.setValue(null);
        tabelaAlunos.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaCadastro::new);
    }
}