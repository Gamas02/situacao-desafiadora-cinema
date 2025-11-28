package model;

import java.time.LocalDateTime;

public class Sessao {

    private Long id;
    private String sala;
    private LocalDateTime horario;
    private EstadoSessao estado;
    private long idIngresso;

    public Sessao() {
    }

    public Sessao(Long id, String sala, LocalDateTime horario, Long idIngresso) {
        this.id = id;
        this.sala = sala;
        this.horario = horario;
        // this.estado = EstadoSessao.ENCERRADO;
        atualizarEstado();
        this.idIngresso = idIngresso;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
        atualizarEstado(); 
    }

    public EstadoSessao getEstado() {
        atualizarEstado();
        return estado;
    }

    public void atualizarEstado() {
        LocalDateTime agora = LocalDateTime.now();

        if (horario.isAfter(agora)) {
            this.estado = EstadoSessao.MARCADO;
        }
        else if (horario.isBefore(agora.minusHours(2))) {
            this.estado = EstadoSessao.ENCERRADO;
        }
        else {
            this.estado = EstadoSessao.EM_ANDAMENTO;
        }
    }

    public long getIdIngresso() {
        return idIngresso;
    }

    @Override
    public String toString() {
        return "Sessao[id=" + id +
                ", sala=" + sala +
                ", horario=" + horario +
                ", estado=" + getEstado() +
                ", idIngresso=" + getIdIngresso() +
                "]";
    }

    public void setEstado(EstadoSessao estado) {
        this.estado = estado;
    }

}
              