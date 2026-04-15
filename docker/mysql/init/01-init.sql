-- Arquivo para dados iniciais.

USE medalerta;
CREATE TABLE Usuario (
    idUsuario INT AUTO_INCREMENT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    enderecoRua VARCHAR(100),
    enderecoNumero INT,
    enderecoComplemento VARCHAR(50),
    enderecoBairro VARCHAR(50),
    enderecoCEP VARCHAR(10),
    enderecoCidade VARCHAR(50),
    enderecoEstado CHAR(02),
    PRIMARY KEY (idUsuario)
);

CREATE TABLE Medicamento (
    idMedicamento INT AUTO_INCREMENT NOT NULL,
    nomeComercial VARCHAR(100) NOT NULL,
    nomeGenerico VARCHAR(100),
    quantidade ENUM('unidade', 'ml'),
    formaUso VARCHAR(100),
    observacao VARCHAR(200),
    PRIMARY KEY (idMedicamento)
);
CREATE TABLE UsuarioMedicamento (
    idUsuario INT NOT NULL,
    idMedicamento INT NOT NULL,
    horarioUso TIME NOT NULL,
    frequenciaUso VARCHAR(50),
    dosagem VARCHAR(50) NOT NULL,
    dataHorarioAlerta DATETIME NOT NULL,
    statusAlerta ENUM('emitido', 'não emitido') NOT NULL,
    dataHorarioConsumo DATETIME,
    confirmacaoConsumo ENUM('sim', 'não') NOT NULL,
    
    PRIMARY KEY (idUsuario, idMedicamento),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idMedicamento) REFERENCES Medicamento(idMedicamento)
);

CREATE TABLE HistoricoConsumo (
    idHistorico INT AUTO_INCREMENT NOT NULL,
    idUsuario INT NOT NULL,
    idMedicamento INT NOT NULL,
    dataHorarioPrevisto DATETIME NOT NULL,
    dataHorarioRealizado DATETIME,
    observacao TEXT,
    PRIMARY KEY (idHistorico),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idMedicamento) REFERENCES Medicamento(idMedicamento)
);

CREATE TABLE Estoque (
    idEstoque INT AUTO_INCREMENT NOT NULL,
    idUsuario INT NOT NULL,
    idMedicamento INT NOT NULL,
    quantidadeAtual DECIMAL(10,2) NOT NULL, -- Ex: 20.00 (pode ser ml ou unidades)
    quantidadeMinima DECIMAL(10,2),        -- Disparar um alerta de "estoque baixo"
    dataUltimaAtualizacao DATETIME,
    
    PRIMARY KEY (idEstoque),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idMedicamento) REFERENCES Medicamento(idMedicamento)
);

--INSERT IGNORE INTO Usuario (nome, telefone, email) VALUES ('Alice', '123456789', 'alice@example.com');
--INSERT IGNORE INTO Medicamento (nomeComercial, nomeGenerico, quantidade, formaUso, observacao) VALUES ('Paracetamol', 'Paracetamol', 'unidade', 'oral', 'Tomar 1 comprimido a cada 6 horas');