package com.smc.simplissimo.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Simplissimo.
 */
@Entity
@Table(name = "simplissimo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Simplissimo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_application")
    private String nom_application;

    @Column(name = "action")
    private String action;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "message")
    private String message;

    @Column(name = "date_demande")
    private LocalDate date_demande;

    @Column(name = "date_retour")
    private LocalDate date_retour;

    @Column(name = "user_id")
    private String userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Simplissimo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom_application() {
        return this.nom_application;
    }

    public Simplissimo nom_application(String nom_application) {
        this.setNom_application(nom_application);
        return this;
    }

    public void setNom_application(String nom_application) {
        this.nom_application = nom_application;
    }

    public String getAction() {
        return this.action;
    }

    public Simplissimo action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPassword() {
        return this.password;
    }

    public Simplissimo password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return this.status;
    }

    public Simplissimo status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public Simplissimo message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate_demande() {
        return this.date_demande;
    }

    public Simplissimo date_demande(LocalDate date_demande) {
        this.setDate_demande(date_demande);
        return this;
    }

    public void setDate_demande(LocalDate date_demande) {
        this.date_demande = date_demande;
    }

    public LocalDate getDate_retour() {
        return this.date_retour;
    }

    public Simplissimo date_retour(LocalDate date_retour) {
        this.setDate_retour(date_retour);
        return this;
    }

    public void setDate_retour(LocalDate date_retour) {
        this.date_retour = date_retour;
    }

    public String getUserId() {
        return this.userId;
    }

    public Simplissimo userId(String userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Simplissimo)) {
            return false;
        }
        return id != null && id.equals(((Simplissimo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Simplissimo{" +
            "id=" + getId() +
            ", nom_application='" + getNom_application() + "'" +
            ", action='" + getAction() + "'" +
            ", password='" + getPassword() + "'" +
            ", status='" + getStatus() + "'" +
            ", message='" + getMessage() + "'" +
            ", date_demande='" + getDate_demande() + "'" +
            ", date_retour='" + getDate_retour() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
