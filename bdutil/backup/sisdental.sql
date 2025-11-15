--
-- PostgreSQL database dump
--

\restrict e3sXyOb3wjKKjqPm6r44jTcDT4pBGBeQYV2o9ZBN6PavaxCuBlRsrnvI3UwZ9SG

-- Dumped from database version 17.6 (Debian 17.6-0+deb13u1)
-- Dumped by pg_dump version 17.6 (Debian 17.6-0+deb13u1)

-- Started on 2025-11-12 20:40:55 -03

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 18309)
-- Name: cons_mat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cons_mat (
    cm_id integer NOT NULL,
    mat_id integer,
    con_id integer,
    cm_quant numeric(2,0)
);


ALTER TABLE public.cons_mat OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 18312)
-- Name: cons_mat_cm_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cons_mat_cm_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cons_mat_cm_id_seq OWNER TO postgres;

--
-- TOC entry 3507 (class 0 OID 0)
-- Dependencies: 218
-- Name: cons_mat_cm_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cons_mat_cm_id_seq OWNED BY public.cons_mat.cm_id;


--
-- TOC entry 219 (class 1259 OID 18313)
-- Name: cons_proc; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cons_proc (
    cp_is integer NOT NULL,
    con_id integer,
    pro_id integer,
    cp_quant numeric(2,0)
);


ALTER TABLE public.cons_proc OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 18316)
-- Name: cons_proc_cp_is_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cons_proc_cp_is_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cons_proc_cp_is_seq OWNER TO postgres;

--
-- TOC entry 3508 (class 0 OID 0)
-- Dependencies: 220
-- Name: cons_proc_cp_is_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cons_proc_cp_is_seq OWNED BY public.cons_proc.cp_is;


--
-- TOC entry 221 (class 1259 OID 18317)
-- Name: consulta; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.consulta (
    con_id integer NOT NULL,
    con_relato text,
    con_data date NOT NULL,
    con_horario numeric(2,0) NOT NULL,
    pac_id integer,
    den_id integer,
    con_efetivado boolean DEFAULT false
);


ALTER TABLE public.consulta OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 18323)
-- Name: consulta_con_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.consulta_con_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.consulta_con_id_seq OWNER TO postgres;

--
-- TOC entry 3509 (class 0 OID 0)
-- Dependencies: 222
-- Name: consulta_con_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.consulta_con_id_seq OWNED BY public.consulta.con_id;


--
-- TOC entry 223 (class 1259 OID 18324)
-- Name: dentista; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dentista (
    den_id integer NOT NULL,
    den_nome character varying(50) NOT NULL,
    den_cro numeric(5,0) NOT NULL,
    den_fone character varying(11),
    den_email character varying(100)
);


ALTER TABLE public.dentista OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 18327)
-- Name: dentista_den_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.dentista_den_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.dentista_den_id_seq OWNER TO postgres;

--
-- TOC entry 3510 (class 0 OID 0)
-- Dependencies: 224
-- Name: dentista_den_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.dentista_den_id_seq OWNED BY public.dentista.den_id;


--
-- TOC entry 225 (class 1259 OID 18328)
-- Name: material; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.material (
    mat_id integer NOT NULL,
    mat_desc character varying(20) NOT NULL,
    mat_preco numeric(9,2)
);


ALTER TABLE public.material OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 18331)
-- Name: material_mat_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.material_mat_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.material_mat_id_seq OWNER TO postgres;

--
-- TOC entry 3511 (class 0 OID 0)
-- Dependencies: 226
-- Name: material_mat_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.material_mat_id_seq OWNED BY public.material.mat_id;


--
-- TOC entry 227 (class 1259 OID 18332)
-- Name: paciente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.paciente (
    pac_id integer NOT NULL,
    pac_cpf character varying(14),
    pac_nome character varying(50) NOT NULL,
    pac_cep character varying(9),
    pac_rua character varying(50),
    pac_numero character varying(10),
    pac_bairro character varying(30),
    pac_cidade character varying(20),
    pac_uf character varying(2),
    pac_fone character varying(11),
    pac_email character varying(100),
    pac_histo text
);


ALTER TABLE public.paciente OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 18337)
-- Name: paciente_pac_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.paciente_pac_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.paciente_pac_id_seq OWNER TO postgres;

--
-- TOC entry 3512 (class 0 OID 0)
-- Dependencies: 228
-- Name: paciente_pac_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.paciente_pac_id_seq OWNED BY public.paciente.pac_id;


--
-- TOC entry 229 (class 1259 OID 18338)
-- Name: procedimento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.procedimento (
    pro_id integer NOT NULL,
    pro_desc character varying(20) NOT NULL,
    pro_tempo numeric(2,0),
    pro_valor numeric(9,2)
);


ALTER TABLE public.procedimento OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 18341)
-- Name: procedimento_pro_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.procedimento_pro_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.procedimento_pro_id_seq OWNER TO postgres;

--
-- TOC entry 3513 (class 0 OID 0)
-- Dependencies: 230
-- Name: procedimento_pro_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.procedimento_pro_id_seq OWNED BY public.procedimento.pro_id;


--
-- TOC entry 231 (class 1259 OID 18342)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    uso_id integer NOT NULL,
    uso_nome character varying(20) NOT NULL,
    uso_nivel numeric(1,0) NOT NULL,
    uso_senha character varying(10) NOT NULL,
    den_id integer
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 18345)
-- Name: usuario_uso_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuario_uso_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuario_uso_id_seq OWNER TO postgres;

--
-- TOC entry 3514 (class 0 OID 0)
-- Dependencies: 232
-- Name: usuario_uso_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuario_uso_id_seq OWNED BY public.usuario.uso_id;


--
-- TOC entry 3309 (class 2604 OID 18346)
-- Name: cons_mat cm_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cons_mat ALTER COLUMN cm_id SET DEFAULT nextval('public.cons_mat_cm_id_seq'::regclass);


--
-- TOC entry 3310 (class 2604 OID 18347)
-- Name: cons_proc cp_is; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cons_proc ALTER COLUMN cp_is SET DEFAULT nextval('public.cons_proc_cp_is_seq'::regclass);


--
-- TOC entry 3311 (class 2604 OID 18348)
-- Name: consulta con_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consulta ALTER COLUMN con_id SET DEFAULT nextval('public.consulta_con_id_seq'::regclass);


--
-- TOC entry 3313 (class 2604 OID 18349)
-- Name: dentista den_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dentista ALTER COLUMN den_id SET DEFAULT nextval('public.dentista_den_id_seq'::regclass);


--
-- TOC entry 3314 (class 2604 OID 18350)
-- Name: material mat_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.material ALTER COLUMN mat_id SET DEFAULT nextval('public.material_mat_id_seq'::regclass);


--
-- TOC entry 3315 (class 2604 OID 18351)
-- Name: paciente pac_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paciente ALTER COLUMN pac_id SET DEFAULT nextval('public.paciente_pac_id_seq'::regclass);


--
-- TOC entry 3316 (class 2604 OID 18352)
-- Name: procedimento pro_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.procedimento ALTER COLUMN pro_id SET DEFAULT nextval('public.procedimento_pro_id_seq'::regclass);


--
-- TOC entry 3317 (class 2604 OID 18353)
-- Name: usuario uso_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario ALTER COLUMN uso_id SET DEFAULT nextval('public.usuario_uso_id_seq'::regclass);


--
-- TOC entry 3486 (class 0 OID 18309)
-- Dependencies: 217
-- Data for Name: cons_mat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cons_mat (cm_id, mat_id, con_id, cm_quant) FROM stdin;
1	1	1	2
2	2	1	1
6	1	1	2
7	2	1	1
\.


--
-- TOC entry 3488 (class 0 OID 18313)
-- Dependencies: 219
-- Data for Name: cons_proc; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cons_proc (cp_is, con_id, pro_id, cp_quant) FROM stdin;
1	1	1	2
5	1	1	1
26	1	1	1
27	1	2	1
\.


--
-- TOC entry 3490 (class 0 OID 18317)
-- Dependencies: 221
-- Data for Name: consulta; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.consulta (con_id, con_relato, con_data, con_horario, pac_id, den_id, con_efetivado) FROM stdin;
1	Limpeza de rotina	2025-11-30	9	1	1	t
7	Limpeza de rotina	2025-11-10	6	1	1	f
8	Aplicação de flúor	2025-11-11	3	1	1	f
9	Avaliação de cárie	2025-11-12	2	1	1	f
30		2025-11-11	0	2	6	f
31		2025-11-11	0	2	7	f
\.


--
-- TOC entry 3492 (class 0 OID 18324)
-- Dependencies: 223
-- Data for Name: dentista; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dentista (den_id, den_nome, den_cro, den_fone, den_email) FROM stdin;
6	Ana Carolina Silva	12345	11987654321	ana.silva@gmail.com
7	Bruno Henrique Costa	23456	21998887777	bruno.costa@hotmail.com
8	Carla Menezes Rocha	34567	31988776655	carla.rocha@gmail.com
9	Diego Santos Lima	45678	71991234567	diego.lima@outlook.com
10	Elaine Ferreira Souza	56789	41999887766	elaine.souza@gmail.com
11	Felipe Augusto Prado	67890	85988776655	felipe.prado@yahoo.com
12	Gabriela Tavares	78901	92988332211	gabriela.tavares@gmail.com
13	Henrique Lopes Almeida	89012	55988774455	henrique.almeida@gmail.com
14	Isabela Moura Santos	90123	86999887766	isabela.santos@icloud.com
15	João Pedro Carvalho	1234	92988775544	joao.carvalho@gmail.com
16	Karen Oliveira Dias	13579	86991112222	karen.dias@gmail.com
17	Lucas Andrade Pereira	24680	92991223344	lucas.pereira@hotmail.com
18	Mariana Costa Lima	11223	48998334455	mariana.lima@gmail.com
19	Nathalia Borges F.	33445	86995554411	nathalia.bf@gmail.com
20	Otávio Souza Ramos	55667	31998776655	otavio.ramos@yahoo.com
1	Osvaldo Alberto	19087	18999978998	osvaldo_dentista@gmail.com
2	Matheus Firmino	12356	18992930458	matheus@email.com
4	Márcia Silva e Silva	89712	12333333333	marcia@email.com
5	Eufrásio Gomes	19928	11111111111	eufrasio@dent.com
21	Ana Clara	2345	11999988877	ana.dentista@gmail.com
22	Bruno Souza	3456	11988877766	bruno.dentista@gmail.com
23	Carla Lima	4567	11977766655	carla.dentista@gmail.com
24	Daniel Costa	5678	11966655544	daniel.dentista@gmail.com
25	Elaine Rocha	6789	11955544433	elaine.dentista@gmail.com
\.


--
-- TOC entry 3494 (class 0 OID 18328)
-- Dependencies: 225
-- Data for Name: material; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.material (mat_id, mat_desc, mat_preco) FROM stdin;
1	Gase	5.80
4	Luvas procedim.	45.00
5	Masc. cirúrgica	30.00
6	Gorro desc.	25.00
7	Avental TNT	4.50
8	Propé desc.	1.20
9	Seringa 5ml	55.00
10	Agulha 30G	40.00
11	Fio sutura 4-0	12.00
12	Gaze estéril	5.50
13	Algodão 500g	12.00
14	Espátula resina	35.00
15	Cunha madeira	8.00
16	Matriz metálica	1.50
17	Porta-matriz	45.00
18	Resina comp. 4g	120.00
19	Ácido fosf. 37%	25.00
20	Adesivo dent.	90.00
21	Cim. ionômero	110.00
22	Papel carbono	2.00
23	Espelho clínico	20.00
24	Pinça clínica	18.00
25	Sonda explor.	15.00
26	Bandeja inox P	35.00
27	Copo desc. 50ml	10.00
28	Papel toalha	25.00
29	Desinfetante 1L	30.00
30	Hipoclorito 1%	10.00
31	Álcool 70%	12.00
32	Sugador desc.	25.00
33	Caneta alta rot.	950.00
34	Broca 1012	8.00
35	Broca 245	7.00
36	Lâmina bisturi	2.50
37	Cabo bisturi	35.00
38	Escova Robinson	4.00
39	Pasta profil.	25.00
40	Sugador metálico	85.00
41	Mang. ar/água	150.00
42	Fotopolimeriz.	950.00
43	Amálgama cáps.	15.00
44	Cim. fosf. zinco	65.00
45	Espátula cimt.	20.00
46	Pote Dappen	8.00
47	Papel articul.	18.00
48	Luva cirúrgica	5.50
49	Máscara N95	7.50
50	Fio retrator	45.00
51	Sugador autoclav.	75.00
52	Caixa perfuro 7L	18.00
53	Saco infectante	2.00
2	Abaixador de língua	0.90
\.


--
-- TOC entry 3496 (class 0 OID 18332)
-- Dependencies: 227
-- Data for Name: paciente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.paciente (pac_id, pac_cpf, pac_nome, pac_cep, pac_rua, pac_numero, pac_bairro, pac_cidade, pac_uf, pac_fone, pac_email, pac_histo) FROM stdin;
1	122.555.998-36	Romulo Bezerra	19950-123	Rua das Flores	10	Bairro das Flores	Indiana	SP	18999999999	romulo@email.com	Paciente com alergia a corticoides.
2	123.456.789-10	Matheus Oliveira	19045-410	Rua Alfredo Pinto	290	Jardim Brasília	Presidente Prudente	SP	18996979697	matheus@email.com	\N
3	321.456.789.01	João Paulo	19999-002	Rua Alemanha	200	Jardim Nova Belém	Anhumas	SP	18992992929	joao@email.com	\N
4	123.456.789-00	Ana Paula Silva	01001-000	Rua das Flores	120	Centro	São Paulo	SP	11987654321	ana.silva@gmail.com	Paciente sem doenças prévias. Realiza consultas de rotina a cada 6 meses.
5	987.654.321-00	Bruno Henrique Costa	22290-240	Av. Atlântica	1500	Copacabana	Rio de Janeiro	RJ	21998887777	bruno.costa@hotmail.com	Relata sensibilidade dentária. Fez clareamento em 2023.
6	321.654.987-11	Carla Menezes Rocha	30140-110	Rua Timbiras	450	Funcionários	Belo Horizonte	MG	31988776655	carla.rocha@gmail.com	Hipertensa controlada. Última consulta em 2024, sem intercorrências.
7	654.987.321-22	Diego Santos Lima	40015-130	Rua Chile	210	Comércio	Salvador	BA	71991234567	diego.lima@outlook.com	Fez extração de siso inferior em 2024. Boa recuperação.
8	159.753.486-33	Elaine Ferreira Souza	80010-100	Rua XV de Novembro	87	Centro	Curitiba	PR	41999887766	elaine.souza@gmail.com	Alergia a penicilina. Em tratamento ortodôntico desde 2023.
9	258.369.147-44	Felipe Augusto Prado	60115-085	Av. Beira Mar	1020	Meireles	Fortaleza	CE	85988776655	felipe.prado@yahoo.com	Paciente com implante unitário no dente 36 realizado em 2022.
10	753.951.852-55	Gabriela Tavares	69005-020	Rua Sete de Setembro	520	Centro	Manaus	AM	92988332211	gabriela.tavares@gmail.com	Histórico de gengivite leve. Faz profilaxia regularly.
11	852.456.159-66	Henrique Lopes Almeida	97010-090	Av. Rio Branco	300	Centro	Santa Maria	RS	55988774455	henrique.almeida@gmail.com	Sem condições sistêmicas. Realizou restauração de resina composta em 2025.
12	951.357.258-77	Isabela Moura Santos	64000-100	Rua Coelho Rodrigues	250	Centro	Teresina	PI	86999887766	isabela.santos@icloud.com	Paciente ansiosa. Realiza acompanhamento periódico para manutenção de prótese fixa.
13	147.258.369-88	João Pedro Carvalho	69075-350	Rua Dom Pedro II	145	São Raimundo	Manaus	AM	92988775544	joao.carvalho@gmail.com	Fez tratamento de canal em 2023. Sem complicações atuais.
14	111.222.333-44	Karen Oliveira Dias	64010-230	Rua das Laranjeiras	215	Centro	Teresina	PI	86991112222	karen.dias@gmail.com	Paciente gestante, acompanhamento preventivo trimestral.
15	222.333.444-55	Lucas Andrade Pereira	69020-010	Av. Getúlio Vargas	180	Adrianópolis	Manaus	AM	92991223344	lucas.pereira@hotmail.com	Fez raspagem periodontal em 2024. Mantém boa higiene oral.
16	333.444.555-66	Mariana Costa Lima	88010-400	Rua Felipe Schmidt	99	Centro	Florianópolis	SC	48998334455	mariana.lima@gmail.com	Sem queixas. Última profilaxia em 2025.
17	444.555.666-77	Nathalia Borges F.	64015-250	Rua Rui Barbosa	310	Centro	Teresina	PI	86995554411	nathalia.bf@gmail.com	Sensibilidade leve em dente 21. Acompanhamento anual.
18	555.666.777-88	Otávio Souza Ramos	30150-140	Av. Contorno	840	Savassi	Belo Horizonte	MG	31998776655	otavio.ramos@yahoo.com	Tratamento ortodôntico finalizado em 2024.
19	666.777.888-99	Paula Mendes Araujo	22775-030	Rua das Hortênsias	155	Barra	Rio de Janeiro	RJ	21990011223	paula.m.araujo@gmail.com	Fez clareamento dental. Retorno em 2026.
20	777.888.999-00	Ricardo Matos Silva	64020-050	Rua Boa Esperança	290	Ilhotas	Teresina	PI	86996665544	ricardo.silva@outlook.com	Diabético tipo 2 controlado. Cuidados com cicatrização.
21	888.999.000-11	Sabrina Torres Melo	49010-180	Rua Itabaiana	400	Centro	Aracaju	SE	79998887766	sabrina.melo@gmail.com	Alergia a ibuprofeno. Sem outras restrições.
22	999.000.111-22	Thiago Rocha Nunes	58010-200	Av. Epitácio Pessoa	500	Centro	João Pessoa	PB	83991234567	thiago.nunes@gmail.com	Paciente atleta. Avaliação para placa miorrelaxante.
23	000.111.222-33	Úrsula Mendes Dias	57010-300	Rua do Comércio	210	Jaraguá	Maceió	AL	82995554433	ursula.dias@gmail.com	Fez implante unitário em 2023. Revisão anual.
24	123.321.456-78	Vinícius Souza Brito	65010-040	Rua Rio Branco	122	Centro	São Luís	MA	98997776655	vinicius.brito@gmail.com	Relata bruxismo noturno. Em uso de placa de contenção.
25	234.432.567-89	Wellington Prado	69900-200	Av. Brasil	500	Centro	Rio Branco	AC	68994443322	wellington.prado@gmail.com	Paciente com hipertensão leve. Acompanhamento regular.
26	345.543.678-90	Xênia Duarte Lima	29010-060	Rua General Osório	210	Centro	Vitória	ES	27992233445	xenia.lima@gmail.com	Reabilitação com prótese total concluída em 2024.
27	456.654.789-01	Yasmin Farias Cruz	69060-020	Rua Pará	350	Nossa Senhora das Graças	Manaus	AM	92993334455	yasmin.cruz@gmail.com	Paciente jovem. Avaliação ortodôntica em andamento.
28	567.765.890-12	Zeca Albuquerque	58040-200	Rua Goiás	420	Tambaú	João Pessoa	PB	83994443322	zeca.albuquerque@gmail.com	Sem histórico relevante. Realizou limpeza em 2025.
29	678.876.901-23	Aline Teixeira	69075-350	Rua São Pedro	280	São Raimundo	Manaus	AM	92995556677	aline.teixeira@hotmail.com	Extração de terceiro molar em 2024. Recuperação ok.
30	789.987.012-34	Breno Figueiredo	64015-250	Rua São Raimundo	105	Centro	Teresina	PI	86997776655	breno.figueiredo@gmail.com	Paciente com periodontite leve. Em acompanhamento.
31	890.098.123-45	Camila Andrade	88010-400	Av. Hercílio Luz	333	Centro	Florianópolis	SC	48992233445	camila.andrade@gmail.com	Acompanhamento ortodôntico ativo. Higiene adequada.
32	901.109.234-56	Daniel Ribeiro	30120-040	Av. Afonso Pena	700	Centro	Belo Horizonte	MG	31995556677	daniel.ribeiro@gmail.com	Paciente assintomático. Faz manutenção semestral.
33	012.210.345-67	Eduarda Lima	22775-030	Rua Margaridas	199	Barra	Rio de Janeiro	RJ	21998886655	eduarda.lima@gmail.com	Sem comorbidades. Última restauração em 2024.
\.


--
-- TOC entry 3498 (class 0 OID 18338)
-- Dependencies: 229
-- Data for Name: procedimento; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.procedimento (pro_id, pro_desc, pro_tempo, pro_valor) FROM stdin;
2	Avaliação inicial	30	150.00
3	Limpeza dental	40	200.00
4	Raspagem supra	50	250.00
5	Raspagem sub	60	300.00
6	Aplic. flúor	20	120.00
7	Restauração res.	45	280.00
8	Restauração amal.	50	250.00
9	Coroa total	60	900.00
10	Faceta resina	40	700.00
11	Faceta porcelana	45	1600.00
12	Extração simples	40	250.00
13	Extração molar	60	400.00
14	Cirurgia ciso	50	800.00
15	Sutura gengival	20	150.00
16	Canal anterior	60	600.00
17	Canal pré-molar	50	700.00
18	Canal molar	45	900.00
19	Clareamento ext.	60	600.00
20	Claream. consult.	45	400.00
21	Profilaxia	30	180.00
22	Pino de fibra	40	350.00
23	Remoção tártaro	50	220.00
24	Polimento	25	150.00
25	Ciment. coroa	30	250.00
26	Prova prótese	40	200.00
27	Prótese total	55	1800.00
28	Prótese parcial	50	1300.00
29	Restaura provis.	30	180.00
30	Controle retorno	20	100.00
31	Urgência dor	30	200.00
32	Ajuste oclusal	25	150.00
33	Tartarectomia	45	250.00
34	Remoção sutura	15	80.00
35	Selante fissura	25	150.00
36	Ajuste prótese	30	180.00
37	Implante unit.	60	1800.00
38	Manut. implante	45	250.00
39	Remoção aparelho	60	350.00
40	Ajuste aparelho	40	200.00
41	Colagem bracket	50	250.00
42	Placa miorrelax.	60	600.00
43	Radiografia	15	100.00
44	Consulta retorno	20	120.00
45	Anestesia local	10	80.00
46	Curativo canal	20	150.00
47	Rebase prótese	40	220.00
48	Desgaste selet.	25	180.00
49	Top. flúor gel	20	100.00
50	Controle sangram.	15	90.00
1	Radiog. panorâmica	20	150.00
\.


--
-- TOC entry 3500 (class 0 OID 18342)
-- Dependencies: 231
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuario (uso_id, uso_nome, uso_nivel, uso_senha, den_id) FROM stdin;
3	s	2	123	\N
5	admin	1	admin123	\N
7	geovana	2	gi25	\N
4	osvaldo	3	alberto	1
6	elaine	3	rocha23	25
9	costabh	3	bhc123	7
15	carla	3	carla123	8
16	root	1	root123	\N
17	karen	3	karen123	16
18	daniela	2	daniela123	\N
8	anaclara	3	clara	21
\.


--
-- TOC entry 3515 (class 0 OID 0)
-- Dependencies: 218
-- Name: cons_mat_cm_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cons_mat_cm_id_seq', 17, true);


--
-- TOC entry 3516 (class 0 OID 0)
-- Dependencies: 220
-- Name: cons_proc_cp_is_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cons_proc_cp_is_seq', 27, true);


--
-- TOC entry 3517 (class 0 OID 0)
-- Dependencies: 222
-- Name: consulta_con_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.consulta_con_id_seq', 31, true);


--
-- TOC entry 3518 (class 0 OID 0)
-- Dependencies: 224
-- Name: dentista_den_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.dentista_den_id_seq', 28, true);


--
-- TOC entry 3519 (class 0 OID 0)
-- Dependencies: 226
-- Name: material_mat_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.material_mat_id_seq', 55, true);


--
-- TOC entry 3520 (class 0 OID 0)
-- Dependencies: 228
-- Name: paciente_pac_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.paciente_pac_id_seq', 34, true);


--
-- TOC entry 3521 (class 0 OID 0)
-- Dependencies: 230
-- Name: procedimento_pro_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.procedimento_pro_id_seq', 51, true);


--
-- TOC entry 3522 (class 0 OID 0)
-- Dependencies: 232
-- Name: usuario_uso_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuario_uso_id_seq', 19, true);


--
-- TOC entry 3319 (class 2606 OID 18355)
-- Name: cons_mat cons_mat_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cons_mat
    ADD CONSTRAINT cons_mat_pkey PRIMARY KEY (cm_id);


--
-- TOC entry 3321 (class 2606 OID 18357)
-- Name: cons_proc cons_proc_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cons_proc
    ADD CONSTRAINT cons_proc_pkey PRIMARY KEY (cp_is);


--
-- TOC entry 3323 (class 2606 OID 18359)
-- Name: consulta consulta_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consulta
    ADD CONSTRAINT consulta_pkey PRIMARY KEY (con_id);


--
-- TOC entry 3325 (class 2606 OID 18361)
-- Name: dentista dentista_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dentista
    ADD CONSTRAINT dentista_pkey PRIMARY KEY (den_id);


--
-- TOC entry 3327 (class 2606 OID 18363)
-- Name: material material_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.material
    ADD CONSTRAINT material_pkey PRIMARY KEY (mat_id);


--
-- TOC entry 3329 (class 2606 OID 18365)
-- Name: paciente paciente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.paciente
    ADD CONSTRAINT paciente_pkey PRIMARY KEY (pac_id);


--
-- TOC entry 3331 (class 2606 OID 18367)
-- Name: procedimento procedimento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.procedimento
    ADD CONSTRAINT procedimento_pkey PRIMARY KEY (pro_id);


--
-- TOC entry 3333 (class 2606 OID 18369)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (uso_id);


--
-- TOC entry 3334 (class 2606 OID 18370)
-- Name: cons_mat cons_mat_con_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cons_mat
    ADD CONSTRAINT cons_mat_con_id_fkey FOREIGN KEY (con_id) REFERENCES public.consulta(con_id) NOT VALID;


--
-- TOC entry 3335 (class 2606 OID 18375)
-- Name: cons_mat cons_mat_mat_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cons_mat
    ADD CONSTRAINT cons_mat_mat_id_fkey FOREIGN KEY (mat_id) REFERENCES public.material(mat_id) NOT VALID;


--
-- TOC entry 3336 (class 2606 OID 18380)
-- Name: cons_proc cons_proc_con_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cons_proc
    ADD CONSTRAINT cons_proc_con_id_fkey FOREIGN KEY (con_id) REFERENCES public.consulta(con_id) NOT VALID;


--
-- TOC entry 3337 (class 2606 OID 18385)
-- Name: cons_proc cons_proc_pro_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cons_proc
    ADD CONSTRAINT cons_proc_pro_id_fkey FOREIGN KEY (pro_id) REFERENCES public.procedimento(pro_id) NOT VALID;


--
-- TOC entry 3338 (class 2606 OID 18390)
-- Name: consulta consulta_den_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consulta
    ADD CONSTRAINT consulta_den_id_fkey FOREIGN KEY (den_id) REFERENCES public.dentista(den_id) NOT VALID;


--
-- TOC entry 3339 (class 2606 OID 18395)
-- Name: consulta consulta_pac_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.consulta
    ADD CONSTRAINT consulta_pac_id_fkey FOREIGN KEY (pac_id) REFERENCES public.paciente(pac_id) NOT VALID;


--
-- TOC entry 3340 (class 2606 OID 18400)
-- Name: usuario fk_usuario_dentista; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT fk_usuario_dentista FOREIGN KEY (den_id) REFERENCES public.dentista(den_id);


-- Completed on 2025-11-12 20:40:55 -03

--
-- PostgreSQL database dump complete
--

\unrestrict e3sXyOb3wjKKjqPm6r44jTcDT4pBGBeQYV2o9ZBN6PavaxCuBlRsrnvI3UwZ9SG

