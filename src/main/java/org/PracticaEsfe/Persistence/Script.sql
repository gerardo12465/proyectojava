CREATE TABLE Usuario (
                         Id INT PRIMARY KEY AUTO_INCREMENT,
                         Nombre VARCHAR(100) NOT NULL,
                         Email VARCHAR(100) UNIQUE NOT NULL,
                         Contraseña VARCHAR(100) NOT NULL
);

CREATE TABLE Autor (
                       Id INT PRIMARY KEY AUTO_INCREMENT,
                       NombreCompleto VARCHAR(100) NOT NULL,
                       Nacionalidad VARCHAR(50)
);

CREATE TABLE Libro (
                       Id INT PRIMARY KEY AUTO_INCREMENT,
                       Titulo VARCHAR(150) NOT NULL,
                       FechaPublicacion DATE,
                       IdAutor INT,
                       FOREIGN KEY (IdAutor) REFERENCES Autor(Id)
);

CREATE TABLE Prestamo (
                          Id INT PRIMARY KEY AUTO_INCREMENT,
                          IdUsuario INT,
                          IdLibro INT,
                          FechaPrestamo DATE NOT NULL,
                          FechaDevolucion DATE,
                          FOREIGN KEY (IdUsuario) REFERENCES Usuario(Id),
                          FOREIGN KEY (IdLibro) REFERENCES Libro(Id)
);
